package com.ball.base.model;

import java.math.BigDecimal;

/**
 * @author JimChery
 */
public class DecimalHandler {
    private BigDecimal value;

    private DecimalHandler() {
        this.value = BigDecimal.ZERO;
    }

    private DecimalHandler(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        this.value = value;
    }

    public DecimalHandler subtract(BigDecimal value) {
        if (value != null) {
            this.value = this.value.subtract(value);
        }
        return this;
    }

    public DecimalHandler add(BigDecimal value) {
        if (value != null) {
            this.value = this.value.add(value);
        }
        return this;
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean hasAmount() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static DecimalHandler instance() {
        return new DecimalHandler();
    }

    public static DecimalHandler instance(BigDecimal value) {
        return new DecimalHandler(value);
    }
}
