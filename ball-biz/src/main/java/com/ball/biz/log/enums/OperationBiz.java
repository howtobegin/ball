package com.ball.biz.log.enums;

/**
 * @author JimChery
 */
public enum OperationBiz {
    ADD_PROXY("PROXY", "ADD_PROXY"),
    ADD_USER("PROXY", "ADD_USER"),
    ADD_PROXY_REFUND_CONFIG("PROXY", "PROXY_REFUND"),
    ADD_REFUND_CONFIG("USER", "USER_REFUND"),
    PROXY_CHANGE_PASSWORD("PROXY", "CHANGE_PASSWORD"),
    ;
    public final String biz;
    public final String bizChild;

    OperationBiz(String biz, String bizChild) {
        this.biz = biz;
        this.bizChild = bizChild;
    }
}
