package com.ball.biz.bet.order.calculate.impl;

import com.ball.biz.bet.order.calculate.bo.CalcResult;
import com.ball.biz.bet.enums.BetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/19 下午5:21
 */
@Slf4j
@Component
public class LoseHalfCalculator extends AbstractCalculator {
    @Override
    public BetResult getBetResult() {
        return BetResult.LOSE_HALF;
    }

    @Override
    protected CalcResult doCalc(BigDecimal betAmount, BigDecimal odds) {
        BigDecimal amount = handleScale(betAmount.multiply(HALF));
        log.info("BetResult {} resultAmount {}", getBetResult(), amount);
        return CalcResult.builder()
                .result(getBetResult())
                .betAmount(betAmount)
                .odds(odds)
                .resultAmount(amount)
                .build();
    }
}
