package com.ball.biz.bet.processor;

import com.ball.biz.bet.enums.HandicapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/22 上午11:55
 */
@Slf4j
@Component
public class OverUnderHalfBetProcessor extends OverUnderBetProcessor {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.OVER_UNDER_HALF;
    }
}
