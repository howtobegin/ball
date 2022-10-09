package com.ball.boss.service.system.model;

/**
 * 锁定类型
 */
public enum LockType {
    /**
     * 系统自动锁定
     */
    SYSTEM(1),
    /**
     * 用户锁定
     */
    MANAGER(2);

    public final int v;

    LockType(int v) {
        this.v = v;
    }

}
