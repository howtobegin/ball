package com.ball.base.model.enums;

/**
 * @author JimChery
 */
public enum YesOrNo {
    YES(1),
    NO(0)
    ;
    public final int v;
    YesOrNo(int v) {
        this.v = v;
    }

    public boolean isMe(Integer v) {
        return v != null && v == this.v;
    }
}
