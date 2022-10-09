package com.ball.boss.service.system.model;

/**
 * 编辑模式
 */
public enum EditMode {
    /**
     * 只读
     */
    READONLY("0"),
    /**
     * 可编辑
     */
    EDITABLE("1");

    public final String v;

    EditMode(String v) {
        this.v = v;
    }

    public static boolean isReadOnly(String v) {
        return READONLY.v.equals(v);
    }
}
