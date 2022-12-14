package com.ball.biz.bet.processor;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.MatchTimeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/22 上午11:30
 */
@Slf4j
@Component
public class CorrectScoreHalfBetProcessor extends CorrectScoreBetProcessor {
    @Value("${bet.score.half.enable:true}")
    private boolean enable;
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.CORRECT_SCORE_HALL;
    }

    @Override
    protected Integer getMatchTimeType() {
        return MatchTimeType.HALF.getCode();
    }

    @Override
    public boolean isEnable() {
        return enable;
    }
}
