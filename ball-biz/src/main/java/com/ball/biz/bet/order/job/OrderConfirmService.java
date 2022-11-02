package com.ball.biz.bet.order.job;

import com.alibaba.fastjson.JSON;
import com.ball.base.exception.AssertException;
import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.processor.BetProcessorHolder;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.IOddsScoreService;
import com.ball.biz.match.service.IOddsService;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhl
 * @date 2022/10/20 下午6:27
 */
@Slf4j
@Getter
@Component
public class OrderConfirmService extends BaseJobService<OrderInfo> {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IOddsService oddsService;
    @Autowired
    private IOddsScoreService oddsScoreService;
    @Autowired
    private ISchedulesService schedulesService;

    @Value("${order.confirm.page.size:100}")
    private int pageSize;
    /**
     * 确认时间，最好大于赔率允许的延迟时间，大于接口请求时间
     */
    @Value("${order.confirm.seconds:10}")
    private int confirmSeconds;

    @Value("${order.confirm.check.rebet:true}")
    private boolean checkRebet;
    @Value("${order.confirm.check.score:true}")
    private boolean checkScore;


    @Override
    public boolean executeOne(OrderInfo data) {
        String bizNo = JSON.parseObject(data.getOddsData()).getString("bizNo");
        if (StringUtils.isEmpty(bizNo)) {
            log.warn("orderId {} bizNo is empty", data.getOrderId());
            return false;
        }
        HandicapType handicapType = HandicapType.parse(data.getHandicapType());

        if (!rebetCheck(data, bizNo, handicapType) || !checkScore(data)) {
            return true;
        }

        // 确认订单
        OrderStatus next = OrderStatus.CONFIRM;
        orderInfoService.updateStatus(data.getOrderId(), OrderStatus.INIT, next);
        return true;
    }

    /**
     * 重走下单校验
     * @param order
     * @param bizNo
     * @param handicapType
     * @return
     */
    private boolean rebetCheck(OrderInfo order, String bizNo, HandicapType handicapType) {
        log.info("orderId {} checkRebet {}", order.getOrderId(), checkRebet);
        if (!checkRebet) {
            return true;
        }
        try {
            // 校验比赛和盘口
            BetProcessorHolder.get(handicapType).betCheck(BetBo.builder()
                    .userNo(order.getUserId())
                    .handicapType(handicapType)
                    .bizNo(bizNo)
                    .betOption(BetOption.valueOf(order.getBetOption()))
                    .betAmount(order.getBetAmount())
                    .betTime(order.getCreateTime())
                    .build(), false);
        } catch (AssertException e) {
            log.error("{}", e.getMessage());
            String reason = "rebet Check error:"+e.getMessage();
            // 取消订单
            orderInfoService.cancel(order.getOrderId(), reason.substring(0, Math.min(128, reason.length())));
            return false;
        }
        return true;
    }

    /**
     * 校验比分
     * @param order
     * @return
     */
    private boolean checkScore(OrderInfo order) {
        log.info("orderId {} checkScore {}", order.getOrderId(), checkScore);
        if (!checkScore) {
            return true;
        }
        // 投注时比分
        Integer homeCurrentScore = order.getHomeCurrentScore();
        Integer awayCurrentScore = order.getAwayCurrentScore();
        // 当前比分
        Schedules schedules = schedulesService.queryOne(order.getMatchId());
        Integer homeScore = schedules.getHomeScore();
        Integer awayScore = schedules.getAwayScore();
        if (!homeCurrentScore.equals(homeScore) || !awayCurrentScore.equals(awayScore)) {
            log.info("orderId {} matchId {} homeCurrentScore {} awayCurrentScore {} homeScore {} awayScore {}",order.getOrderId(),order.getMatchId(),homeCurrentScore,awayCurrentScore,homeScore,awayScore);
            String reason = MessageFormat.format("score change:{0}:{1}->{2}:{3}",homeCurrentScore,awayCurrentScore,homeScore,awayScore);
            // 取消订单
            orderInfoService.cancel(order.getOrderId(), reason);
            return false;
        }
        return true;
    }

    @Override
    public List<OrderInfo> fetchData() {
        log.info("maxCallbackId {} pageSize {}", maxCallbackId, pageSize);
        return orderInfoService.lambdaQuery()
                .gt(OrderInfo::getId, maxCallbackId)
                .lt(OrderInfo::getCreateTime, LocalDateTime.now().minusSeconds(confirmSeconds))
                .eq(OrderInfo::getStatus, OrderStatus.INIT.getCode())
                .orderByAsc(OrderInfo::getId)
                .page(new Page<>(1, pageSize))
                .getRecords();
    }

    @Override
    public Long getId(OrderInfo data) {
        return data.getId();
    }

    @Override
    public String getBizNo(OrderInfo data) {
        return data.getOrderId();
    }
}
