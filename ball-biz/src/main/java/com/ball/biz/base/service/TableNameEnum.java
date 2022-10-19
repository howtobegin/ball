package com.ball.biz.base.service;

/**
 * @author littlehow
 */
public enum TableNameEnum {
    USER_INFO("user_info"),
    ;
    public final String tableName;

    TableNameEnum(String tableName) {
        this.tableName = tableName;
    }
}
