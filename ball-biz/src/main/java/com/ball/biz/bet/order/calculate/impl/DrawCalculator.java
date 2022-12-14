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
public class DrawCalculator extends AbstractCalculator {
    @Override
    public BetResult getBetResult() {
        return BetResult.DRAW;
    }

    @Override
    protected CalcResult doCalc(BigDecimal betAmount, BigDecimal odds) {
        BigDecimal amount = BigDecimal.ZERO;
        log.info("BetResult {} resultAmount {}", getBetResult(), amount);
        return CalcResult.builder()
                .result(getBetResult())
                .betAmount(betAmount)
                .resultAmount(amount)
                .build();
    }

    @Override
    protected BigDecimal getBackwaterBaseAmount(CalcResult calcResult) {
        return BigDecimal.ZERO;
    }
}
