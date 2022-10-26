package com.ball.biz.bet.order.job;

import com.alibaba.fastjson.JSON;
import com.ball.base.exception.AssertException;
import com.ball.biz.bet.BetCheckAssist;
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
    private BetCheckAssist betCheckAssist;
    @Autowired
    private ISchedulesService schedulesService;

    @Value("${order.confirm.page.size:100}")
    private int pageSize;
    /**
     * 确认时间
     */
    @Value("${order.confirm.seconds:10}")
    private int confirmSeconds;

    @Override
    public boolean executeOne(OrderInfo data) {
        String bizNo = JSON.parseObject(data.getOddsData()).getString("bizNo");
        HandicapType handicapType = HandicapType.parse(data.getHandicapType());

        try {
            // 校验比赛和盘口
            BetProcessorHolder.get(handicapType).betCheck(BetBo.builder()
                    .userNo(data.getUserId())
                    .handicapType(handicapType)
                    .bizNo(bizNo)
                    .betOption(BetOption.valueOf(data.getBetOption()))
                    .betAmount(data.getBetAmount())
                    .build());
        } catch (AssertException e) {
            log.error("{}", e.getMessage());
            // 取消订单
            orderInfoService.cancel(data.getOrderId());
            return true;
        }
        // 投注时比分
        Integer homeCurrentScore = data.getHomeCurrentScore();
        Integer awayCurrentScore = data.getAwayCurrentScore();
        // 当前比分
        Schedules schedules = schedulesService.queryOne(data.getMatchId());
        Integer homeScore = schedules.getHomeScore();
        Integer awayScore = schedules.getAwayScore();
        if (!homeCurrentScore.equals(homeScore) || !awayCurrentScore.equals(awayScore)) {
            // 取消订单
            orderInfoService.cancel(data.getOrderId());
            return true;
        }

        // 确认订单
        OrderStatus next = OrderStatus.CONFIRM;
        orderInfoService.updateStatus(data.getOrderId(), OrderStatus.INIT, next);
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
