package com.ball.boss.service.system.model;

/**
 * 操作类型
 */
public enum OperationType {
    /**
     * 新增
     */
    ADD("新增"),
    /**
     * 修改
     */
    UPDATE("修改"),
    /**
     * 删除
     */
    DELETE("删除");

    public final String v;

    OperationType(String v) {
        this.v = v;
    }

    public static String getDescription(String type) {
        switch (type) {
            case "ADD":
                return ADD.v;
            case "UPDATE":
                return UPDATE.v;
            case "DELETE":
                return DELETE.v;
            default:
                return "未知";
        }
    }
}
