package com.ball.biz.enums;

/**
 * @author littlehow
 */
public enum UserTypeEnum {
    GENERAL(1, 1, false),
    PROXY_ONE(5, 6, true),
    PROXY_TWO(6, 7, true),
    PROXY_THREE(7, 7, true),
    ;
    public final int v;

    public final int next;

    public final boolean createProxy;

    UserTypeEnum(int v, int next, boolean createProxy) {
        this.v = v;
        this.next = next;
        this.createProxy = createProxy;
    }

    public static UserTypeEnum valueOf(int v) {
        switch (v) {
            case 5 : return  PROXY_ONE;
            case 6 : return PROXY_TWO;
            case 7 : return PROXY_THREE;
            case 1:
                default:return GENERAL;
        }
    }
}
