package com.ball.biz.bet.order.calculate.impl;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.order.calculate.Calculator;
import com.ball.biz.bet.order.calculate.CalculatorHolder;
import com.ball.biz.bet.order.calculate.bo.CalcResult;
import com.ball.biz.exception.BizErrCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/19 下午5:53
 */
@Slf4j
public abstract class AbstractCalculator implements Calculator, InitializingBean {
    protected static final BigDecimal HALF = BigDecimal.valueOf(0.5);
    @Value("${resultAmount.scale:2}")
    protected int scale;

    @Override
    public CalcResult calc(BigDecimal betAmount, BigDecimal odds) {
        log.info("betAmount {} rate {}", betAmount, odds);
        calcCheck(betAmount, odds);
        return doCalc(betAmount, odds);
    }

    protected abstract CalcResult doCalc(BigDecimal betAmount, BigDecimal odds);

    protected void calcCheck(BigDecimal betAmount, BigDecimal rate) {
        BizAssert.notNull(betAmount, BizErrCode.PARAM_ERROR_DESC,"betAmount");
        BizAssert.notNull(rate, BizErrCode.PARAM_ERROR_DESC,"rate");

        BizAssert.isTrue(betAmount.compareTo(BigDecimal.ZERO) > 0, BizErrCode.PARAM_ERROR_DESC,"betAmount");
        BizAssert.isTrue(rate.compareTo(BigDecimal.ZERO) > 0, BizErrCode.PARAM_ERROR_DESC,"rate");
    }

    protected BigDecimal handleScale(BigDecimal amount) {
        return amount.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CalculatorHolder.register(getBetResult(), this);
    }
}
