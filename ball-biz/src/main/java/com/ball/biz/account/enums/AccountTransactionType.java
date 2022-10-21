package com.ball.biz.account.enums;

/**
 * @author littlehow
 */
public enum AccountTransactionType {
    INIT,
    USER_ADJUSTMENT_IN,//调账
    USER_ADJUSTMENT_OUT,//调账
    AGENT_ADJUSTMENT_IN,//调账
    AGENT_ADJUSTMENT_OUT,//调账
    DEPOSIT,//充值
    WITHDRAW,//提现
    TRADE//交易
    ;
}
