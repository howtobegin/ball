package com.ball.biz.bet.order.job;

import com.ball.base.model.enums.YesOrNo;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单比赛投注结算（半场完，完成，取消，中断）
 *
 * 根据比赛状态ScheduleStatus，计算盘口结果，结算用户投注
 * @author lhl
 * @date 2022/10/20 下午6:39
 */
@Slf4j
@Getter
@Component
public class OrderMatchSettleService extends BaseJobService<OrderInfo> {
    @Autowired
    private IOrderInfoService orderInfoService;

    @Value("${order.match.settle.page.size:100}")
    private int pageSize;
    private volatile Long maxCallbackId = 0L;

    @Override
    public boolean executeOne(OrderInfo data) {
        log.info("ScheduleStatus {}", data.getScheduleStatus());
        ScheduleStatus status = ScheduleStatus.parse(data.getScheduleStatus());
        BetResult betResult = null;
        switch (status) {
            case HALF_TIME_BREAK:
            case FINISHED:
                // TODO 正常结算订单
                break;
            case CANCELLED:
            case TERMINATED:
            case INTERRUPTED:
                // TODO 取消订单？
                break;
                default:
                    break;
        }
        // TODO 更新OrderMatch状态
        // TODO 根据投注结果结算订单



        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public List<OrderInfo> fetchData() {
        log.info("maxCallbackId {} pageSize {}", maxCallbackId, pageSize);
        List<Integer> statuses = Lists.newArrayList(
                ScheduleStatus.HALF_TIME_BREAK.getCode(),
                ScheduleStatus.FINISHED.getCode(),
                ScheduleStatus.CANCELLED.getCode(),
                ScheduleStatus.TERMINATED.getCode(),
                ScheduleStatus.INTERRUPTED.getCode()
        );
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
}
