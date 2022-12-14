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
public class LoseCalculator extends AbstractCalculator {
    @Override
    public BetResult getBetResult() {
        return BetResult.LOSE;
    }

    @Override
    protected CalcResult doCalc(BigDecimal betAmount, BigDecimal odds) {
        BigDecimal amount = BigDecimal.ZERO.subtract(betAmount);
        log.info("BetResult {} resultAmount {}", getBetResult(), amount);
        return CalcResult.builder()
                .result(getBetResult())
                .betAmount(betAmount)
                .odds(odds)
                .resultAmount(amount)
                .build();
    }

    @Override
    protected BigDecimal getBackwaterBaseAmount(CalcResult calcResult) {
        return calcResult.getBetAmount();
    }
}
