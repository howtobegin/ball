package com.ball.biz.bet.order.settle.parse.handicap;

import com.ball.biz.bet.enums.HandicapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/24 下午2:36
 */
@Slf4j
@Component
public class HandicapHalfBetAllParser extends HandicapBetAllParser {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP_HALF;
    }
}
