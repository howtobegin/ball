package com.ball.biz.bet.order.settle.parse.handicap;

import com.ball.biz.bet.enums.HandicapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/24 下午2:37
 */
@Slf4j
@Component
public class HandicapHalfBetAvgOneHalfParser extends HandicapBetAvgOneHalfParser {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP_HALF;
    }
}
