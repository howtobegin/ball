package com.ball.biz.enums;

/**
 * @author littlehow
 */
public enum UserTypeEnum {
    GENERAL(1),
    PROXY_ONE(5),
    PROXY_TWO(6),
    PROXY_THREE(7),
    ;
    public final int v;
    UserTypeEnum(int v) {
        this.v = v;
    }
}
