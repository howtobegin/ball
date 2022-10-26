package com.ball.boss.exception;


import com.ball.base.exception.IBizErrCode;

/**
 * @author JimChery
 */
public enum BossErrorCode implements IBizErrCode {
    USER_OR_PASSWORD_ERROR("b101", "account or password error"),
    USER_ACCOUNT_LOCKED("b102", "The account has been locked"),
    DATA_ERROR("b103", "Data error"),
    USER_NOT_EXISTS("b104", "User not exists"),
    UPDATE_FAIL("b105", "Update fail"),
    DATA_NOT_EXISTS("b106", "Data not exists"),
    ROLE_LOCKED("b107", "The role has been locked"),
    DATA_NOT_EDITABLE("b108", "Data not editable"),
    HAS_CHILD("b109", "Possess subordinate data and cannot be deleted"),
    HAS_OCCUPIED("b110", "Has been occupied"),
    USER_NOT_ROLE("b111", "The user hasn't assigned a role yet"),
    USER_NOT_MENU("b112", "The user hasn't assigned a menu yet"),
    DATA_EXISTS("b113", "The data {0} already exists"),
    ORIGIN_PASSWORD_ERROR("b114", "Original password error"),
    NOT_PERMISSION("b115", "Insufficient permissions"),

    ;

    private final String code;
    private final String desc;

    BossErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
