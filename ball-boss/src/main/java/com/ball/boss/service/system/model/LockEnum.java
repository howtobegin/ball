package com.ball.boss.service.system.model;

/**
 * 锁定类型
 */
public enum LockEnum {
    /**
     * 已锁定
     */
    LOCKED("1"),
    /**
     * 未锁定
     */
    UNLOCK("0");

    public final String v;

    LockEnum(String v) {
        this.v = v;
    }

    public static boolean isNotLock(String v) {
        return UNLOCK.v.equals(v);
    }
}
