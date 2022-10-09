package com.ball.boss.service.system.model;

/**
 * 菜单源
 */
public enum MenuSource {
    /**
     * 系统初始化
     */
    SYSTEM_INIT("1"),
    /**
     * 用户添加
     */
    USER_ADD("2");
    public final String v;

    MenuSource(String v) {
        this.v = v;
    }

    public static boolean isSystemInit(String value) {
        return SYSTEM_INIT.v.equals(value);
    }

    public static boolean isUserAdd(String value) {
        return USER_ADD.v.equals(value);
    }
}
