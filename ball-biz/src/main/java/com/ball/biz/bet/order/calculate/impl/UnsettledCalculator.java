package com.ball.biz.bet.order.calculate.impl;

import com.ball.biz.bet.order.calculate.bo.CalcResult;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/19 下午5:21
 */
@Slf4j
@Component
public class UnsettledCalculator extends AbstractCalculator {
    @Override
    public BetResult getBetResult() {
        return BetResult.UNSETTLED;
    }

    @Override
    protected CalcResult doCalc(BigDecimal betAmount, BigDecimal odds) {
        throw new BizException(BizErrCode.BET_RESULT_UNSETTLED);
    }

    @Override
    protected BigDecimal getBackwaterBaseAmount(CalcResult calcResult) {
        return BigDecimal.ZERO;
    }
}
