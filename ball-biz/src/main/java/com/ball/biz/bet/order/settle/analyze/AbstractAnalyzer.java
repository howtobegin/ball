package com.ball.biz.bet.order.settle.analyze;

import com.ball.base.model.enums.YesOrNo;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author lhl
 * @date 2022/10/20 下午7:28
 */
@Slf4j
public abstract class AbstractAnalyzer implements Analyzer, InitializingBean {
    @Autowired
    private ISchedulesService schedulesService;

    @Override
    public BetResult settleBetResult(OrderInfo order) {
        log.info("analyzer {} orderId {}", this.getClass().getSimpleName(), order.getOrderId());
        // 比赛客观结果
        Schedules schedules = schedulesService.queryOne(order.getMatchId());
        // 比赛是否满足结算条件
        boolean satisfied = satisfiedSettle(order, schedules);
        log.info("analyzer {} orderId {} matchId {} satisfied {}", this.getClass().getSimpleName(), order.getOrderId(), order.getMatchId(), satisfied);
        if (!satisfied) {
            return BetResult.UNSETTLED;
        }
        return doAnalyze(order, schedules);
    }

    /**
     * 比赛是否满足条件
     */
    protected boolean satisfiedSettle(OrderInfo order, Schedules schedules) {
        log.info("orderId {} matchId {} status {}", order.getOrderId(), schedules.getMatchId(), schedules.getStatus());
        boolean unsettled = YesOrNo.NO.isMe(order.getSettleStatus());
        return unsettled && isFinish(schedules);
    }

    /**
     * 半场盘口，需重写此方法
     * @param schedules
     * @return
     */
    protected boolean isFinish(Schedules schedules) {
        return ScheduleStatus.FINISHED.isMe(schedules.getStatus());
    }

    /**
     * 分析用户投注结果
     * @param orderMatch
     * @param schedules
     * @return
     */
    protected abstract BetResult doAnalyze(OrderInfo orderMatch, Schedules schedules);

    @Override
    public void afterPropertiesSet() throws Exception {
        AnalyzerHolder.register(getHandicapType(), this);
    }
}
