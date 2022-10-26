package com.ball.base.model;

/**
 * @author JimChery
 */
public class IntHandler {
    private int value;

    private IntHandler() {
        this.value = 0;
    }

    private IntHandler(Integer value) {
        if (value != null) {
            this.value = value;
        } else {
            this.value = 0;
        }
    }

    public IntHandler subtract(Integer value) {
        value = value == null ? 0 : value;
        this.value = this.value - value;
        return this;
    }

    public IntHandler add(Integer value) {
        value = value == null ? 0 : value;
        this.value = this.value + value;
        return this;
    }

    public int getValue() {
        return value;
    }

    public boolean hasAmount() {
        return value > 0;
    }

    public static IntHandler instance() {
        return new IntHandler();
    }

    public static IntHandler instance(Integer value) {
        return new IntHandler(value);
    }

}
