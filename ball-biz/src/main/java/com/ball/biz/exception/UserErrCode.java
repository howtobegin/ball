package com.ball.biz.exception;


import com.ball.base.exception.IBizErrCode;

/**
 * @author JimChery
 */
public enum UserErrCode implements IBizErrCode {
    KYC_INFORMATION_ERROR("U1001", "kyc信息异常"),
    KYC_NOT_CERT("U1003", "kyc未认证"),
    KYC_CERT_EXPIRED("U1004", "kyc认证已过期"),
    KYC_EXISTS("U1006", "kyc信息已存在"),
    KYC_AUDITING("U1007", "kyc信息审核中"),
    ;
    private String code;

    private String desc;

    UserErrCode(String code, String desc) {
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
