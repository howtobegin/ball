package com.ball.biz.bet.order.settle.parse.overunder;

import com.ball.biz.bet.enums.HandicapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/24 下午3:15
 */
@Slf4j
@Component
public class OverUnderHalfAllParser extends OverUnderAllParser {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.OVER_UNDER_HALF;
    }
}
