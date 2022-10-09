package com.ball.boss.service.system.model;

/**
 * 用户类型
 */
public enum UserType {
    /**
     * 业务员
     */
    OPERATOR("1", 1),
    /**
     * 管理员
     */
    ADMIN("2", 2),
    /**
     * 系统内置人员
     */
    SYSTEM("3", 3);

    public final String v;
    public final int iv;

    UserType(String v, int iv) {
        this.v = v;
        this.iv = iv;
    }

    public static UserType value(String value) {
        switch (value) {
            case "1":
                return OPERATOR;
            case "2":
                return ADMIN;
            case "3":
                return SYSTEM;
            default:
                throw new IllegalArgumentException("in valid enum value " + value);
        }
    }

    public boolean biggerThan(UserType userType) {
        return this.iv >= userType.iv;
    }
}
