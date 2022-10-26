package com.ball.common.exception;


import com.ball.base.exception.IBizErrCode;

/**
 * @author JimChery
 */
public enum  CommonErrorCode implements IBizErrCode {
    SAVE_FILE_ERROR("C8001", "save file error"),
    FILE_NOT_EXISTS("C8002", "file not exists"),
    READ_FILE_ERROR("C8003", "read file error"),

    MESSAGE_TEMPLATE_NOT_EXISTS("C8004", "message template not exists"),
    MESSAGE_SEND_FREQUENTLY("C8005", "message send frequently"),
    EMAIL_CONFIG_ERROR("C8006", "email config error"),
    SMS_CONFIG_ERROR("C8007", "sms config error"),
    SMS_SEND_FAIL("C8008", "sms send fail"),
    MESSAGE_CODE_EXPIRED("C8009", "message code expired"),
    MESSAGE_CODE_ERROR("C8010", "message code error"),
    EMAIL_SEND_FAIL("C8011", "email send fail"),

    HTTP_CLIENT_REQUEST_FAIL("C8050", "http client request fail"),
    GOOGLE_AUTH_TIMEOUT("C8060", "google auth secret time out"),
    GOOGLE_AUTH_CODE_ERROR("C8061", "google auth code error"),
    ;

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 描述说明
     */
    private String desc;

    CommonErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
