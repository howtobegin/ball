package com.ball.biz.bet.order.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/26 上午10:36
 */
@Setter
@Getter
public class ProxyAmount {
    private BigDecimal amount;

    private BigDecimal proxy1Percent;

    private BigDecimal proxy2Percent;

    private BigDecimal proxy3Percent;

    private BigDecimal proxy1Amount;

    private BigDecimal proxy2Amount;

    private BigDecimal proxy3Amount;

    @Builder
    public ProxyAmount(BigDecimal amount, BigDecimal proxy1Percent, BigDecimal proxy2Percent, BigDecimal proxy3Percent, BigDecimal proxy1Amount, BigDecimal proxy2Amount, BigDecimal proxy3Amount) {
        this.amount = amount;
        this.proxy1Percent = proxy1Percent;
        this.proxy2Percent = proxy2Percent;
        this.proxy3Percent = proxy3Percent;
        this.proxy1Amount = proxy1Amount;
        this.proxy2Amount = proxy2Amount;
        this.proxy3Amount = proxy3Amount;
    }
}
