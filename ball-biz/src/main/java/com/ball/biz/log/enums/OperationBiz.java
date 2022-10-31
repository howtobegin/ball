package com.ball.biz.log.enums;

/**
 * @author JimChery
 */
public enum OperationBiz {
    ADD_PROXY("PROXY", "ADD_PROXY"),
    ADD_USER("USER", "ADD_USER"),
    ADD_PROXY_REFUND_CONFIG("PROXY", "PROXY_REFUND"),
    ADD_REFUND_CONFIG("USER", "USER_REFUND"),
    ;
    public final String biz;
    public final String bizChild;

    OperationBiz(String biz, String bizChild) {
        this.biz = biz;
        this.bizChild = bizChild;
    }
}
