package com.ball.boss.service.system.model;

/**
 * 角色类型
 */
public enum RoleType {
    /**
     * 业务角色
     */
    BIZ("1"),
    /**
     * 管理角色
     */
    ADMIN("2"),
    /**
     * 系统内置角色
     */
    SYSTEM("3");

    public final String v;

    RoleType(String v) {
        this.v = v;
    }

    public static boolean isSystem(String v) {
        return SYSTEM.v.equals(v);
    }
}
