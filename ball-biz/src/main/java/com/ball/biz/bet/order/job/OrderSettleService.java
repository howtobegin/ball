package com.ball.biz.bet.order.job;

import com.ball.base.model.enums.YesOrNo;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.bet.order.settle.analyze.AnalyzerHolder;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全场
 * 订单比赛投注结算
 *
 * 根据比赛状态ScheduleStatus，计算盘口结果，结算用户投注
 * @author lhl
 * @date 2022/10/20 下午6:39
 */
@Slf4j
@Getter
@Component
public class OrderSettleService extends BaseJobService<OrderInfo> {
    @Autowired
    private IOrderInfoService orderInfoService;

    @Value("${order.match.settle.page.size:100}")
    private int pageSize;

    @Override
    public boolean executeOne(OrderInfo data) {
        Integer scheduleStatus = data.getScheduleStatus();
        log.info("ScheduleStatus {}", scheduleStatus);
        HandicapType handicapType = HandicapType.parse(data.getHandicapType());
        log.info("orderId {} handicapType {}", data.getOrderId(), handicapType);
        if (handicapType == null) {
            return false;
        }
        AnalyzeResult analyzeResult = AnalyzerHolder.get(handicapType).analyze(data);
        orderInfoService.settled(data.getOrderId(), analyzeResult);
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public List<OrderInfo> fetchData() {
        log.info("maxCallbackId {} pageSize {}", maxCallbackId, pageSize);
        // 上半场结算状态
        List<Integer> statuses = ScheduleStatus.halfSettleCodes();
        return orderInfoService.lambdaQuery()
                .gt(OrderInfo::getId, maxCallbackId)
                // 比赛半场完，完成，取消，中断
                .in(OrderInfo::getScheduleStatus, statuses)
                // 未结算
                .eq(OrderInfo::getSettleStatus, YesOrNo.NO.v)
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
