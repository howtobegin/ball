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
public class WinHalfCalculator extends AbstractCalculator {
    @Override
    public BetResult getBetResult() {
        return BetResult.WIN_HALF;
    }

    @Override
    protected CalcResult doCalc(BigDecimal betAmount, BigDecimal odds) {
        BigDecimal half = betAmount.multiply(HALF);
        BigDecimal win = handleScale(half.multiply(odds));
        BigDecimal resultAmount = handleScale(half.add(win));
        log.info("BetResult {} half {} win {} resultAmount {}", getBetResult(), half, win, resultAmount);
        return CalcResult.builder()
                .result(getBetResult())
                .betAmount(betAmount)
                .odds(odds)
                .resultAmount(resultAmount)
                .build();
    }

    @Override
    protected BigDecimal getBackwaterBaseAmount(CalcResult calcResult) {
        return handleScale(calcResult.getBetAmount().multiply(HALF));
    }
}
