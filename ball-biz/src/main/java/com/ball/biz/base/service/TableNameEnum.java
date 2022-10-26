package com.ball.biz.base.service;

/**
 * @author JimChery
 */
public enum TableNameEnum {
    USER_INFO("user_info"),
    ;
    public final String tableName;

    TableNameEnum(String tableName) {
        this.tableName = tableName;
    }
}
