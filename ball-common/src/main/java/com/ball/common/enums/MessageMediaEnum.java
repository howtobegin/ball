package com.ball.common.enums;

/**
 * @author littlehow
 */
public enum MessageMediaEnum {
    PHONE(1),
    EMAIL(2)
    ;
    public final int v;
    MessageMediaEnum(int v) {
        this.v = v;
    }

    public boolean isPhone() {
        return this == PHONE;
    }
}
