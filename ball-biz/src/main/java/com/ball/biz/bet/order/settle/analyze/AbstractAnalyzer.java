package com.ball.biz.bet.order.settle.analyze;

import com.ball.base.model.enums.YesOrNo;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.MatchTimeType;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


/**
 * @author lhl
 * @date 2022/10/20 下午7:28
 */
@Slf4j
public abstract class AbstractAnalyzer implements Analyzer, InitializingBean {
    @Autowired
    private ISchedulesService schedulesService;

    /**
     * 校验比赛状态类型：1 校验订单上的比赛状态；2 比赛上的状态；3 都校验
     * @param order
     * @return
     */
    @Value("${analyzer.check.status.dataType:1}")
    private int checkStatusType;

    @Override
    public AnalyzeResult analyze(OrderInfo order) {
        log.info("analyzer {} orderId {}", this.getClass().getSimpleName(), order.getOrderId());
        // 比赛客观结果
        Schedules schedules = schedulesService.queryOne(order.getMatchId());
        // 比赛是否满足结算条件
        boolean satisfied = satisfiedSettle(order, schedules);
        log.info("analyzer {} orderId {} matchId {} satisfied {}", this.getClass().getSimpleName(), order.getOrderId(), order.getMatchId(), satisfied);
        if (!satisfied) {
            return AnalyzeResult.builder()
                    .betResult(BetResult.UNSETTLED)
                    .build();
        }
        return doAnalyze(order, schedules);
    }

    /**
     * 比赛是否满足条件
     */
    protected boolean satisfiedSettle(OrderInfo order, Schedules schedules) {
        log.info("orderId {} matchId {} status {}", order.getOrderId(), schedules.getMatchId(), schedules.getStatus());
        // 结算状态和订单状态都是未结算
        boolean unsettled = YesOrNo.NO.isMe(order.getSettleStatus()) && OrderStatus.CONFIRM.isMe(order.getStatus());
        return unsettled && isFinish(order, schedules);
    }

    /**
     * 区分全场或半场盘口
     * @param schedules
     * @return
     */
    protected boolean isFinish(OrderInfo order, Schedules schedules) {
        if (checkStatusType < 3) {
            Integer checkStatus = checkStatusType == 1 ? order.getScheduleStatus() : schedules.getStatus();
            // 全场
            if (MatchTimeType.FULL == getHandicapType().getMatchTimeType()) {
                return ScheduleStatus.FINISHED.isMe(checkStatus);
            }
            // 半场
            return ScheduleStatus.halfSettleCodes().contains(checkStatus);
        } else {
            Integer checkStatus1 = order.getScheduleStatus();
            Integer checkStatus2 = schedules.getStatus();
            // 全场
            if (MatchTimeType.FULL == getHandicapType().getMatchTimeType()) {
                return ScheduleStatus.FINISHED.isMe(checkStatus1) && ScheduleStatus.FINISHED.isMe(checkStatus2);
            }
            // 半场
            return ScheduleStatus.halfSettleCodes().contains(checkStatus1) && ScheduleStatus.halfSettleCodes().contains(checkStatus2);
        }
    }

    /**
     * 分析用户投注结果
     * @param orderMatch
     * @param schedules
     * @return
     */
    protected abstract AnalyzeResult doAnalyze(OrderInfo orderMatch, Schedules schedules);

    @Override
    public void afterPropertiesSet() throws Exception {
        AnalyzerHolder.register(getHandicapType(), this);
    }
}
