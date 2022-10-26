package com.ball.biz.log.enums;

/**
 * @author JimChery
 */
public enum OperationBiz {
    ADD_PROXY("PROXY", "ADD_PROXY"),
    ADD_USER("PROXY", "ADD_USER"),
    ;
    public final String biz;
    public final String bizChild;

    OperationBiz(String biz, String bizChild) {
        this.biz = biz;
        this.bizChild = bizChild;
    }
}
