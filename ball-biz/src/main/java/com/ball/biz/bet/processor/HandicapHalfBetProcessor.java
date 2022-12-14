package com.ball.biz.bet.processor;

import com.ball.biz.bet.enums.HandicapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/22 上午11:55
 */
@Slf4j
@Component
public class HandicapHalfBetProcessor extends HandicapBetProcessor {
    @Value("${bet.handicap.half.enable:true}")
    private boolean enable;
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP_HALF;
    }

    @Override
    protected boolean isEnable() {
        return enable;
    }
}
