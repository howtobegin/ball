package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.match.entity.Schedules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 波胆半场
 * 最后得分取上半场得分
 *
 * @author lhl
 * @date 2022/10/22 上午9:33
 */
@Slf4j
@Component
public class CorrectScoreHalfAnalyzer extends CorrectScoreAnalyzer {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.CORRECT_SCORE_HALL;
    }

    @Override
    protected Integer getAwayLastScore(Schedules schedules) {
        return schedules.getHomeHalfScore();
    }

    @Override
    protected Integer getHomeLastScore(Schedules schedules) {
        return schedules.getAwayHalfScore();
    }
}
