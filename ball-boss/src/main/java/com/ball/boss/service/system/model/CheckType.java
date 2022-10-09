package com.ball.boss.service.system.model;

public enum CheckType {
    /**
     * 初始态
     */
    INIT(0),
    /**
     * 初审
     */
    FIRST_TRIAL(1),
    /**
     * 复审
     */
    RECHECK(2);

    public final int v;

    CheckType(int v) {
        this.v = v;
    }
}
