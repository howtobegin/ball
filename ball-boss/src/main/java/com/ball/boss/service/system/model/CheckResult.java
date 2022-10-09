package com.ball.boss.service.system.model;

public enum CheckResult {
    /**
     * 通过
     */
    PASS(1),
    /**
     * 不通过
     */
    NO_PASS(2);

    public final int v;

    CheckResult(int v) {
        this.v = v;
    }

    public static CheckResult value(int v) {
        switch (v) {
            case 1:
                return PASS;
            case 2:
                return NO_PASS;
            default:
                throw new IllegalArgumentException("invalid value " + v);
        }
    }
}
