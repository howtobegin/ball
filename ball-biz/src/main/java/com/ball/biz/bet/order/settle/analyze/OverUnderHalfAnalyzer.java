package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.match.entity.Schedules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 大小半场
 * 总分取上半场得分
 *
 * @author lhl
 * @date 2022/10/21 上午11:06
 */
@Slf4j
@Component
public class OverUnderHalfAnalyzer extends OverUnderAnalyzer {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.OVER_UNDER_HALF;
    }

    @Override
    protected Integer getTotalScore(Schedules schedules) {
        return schedules.getHomeHalfScore() + schedules.getAwayHalfScore();
    }
}
