package com.ball.biz.exception;


import com.ball.base.exception.IBizErrCode;

public enum BizErrCode implements IBizErrCode {

    DATA_ERROR("14000", "data error"),
    DATA_NOT_EXISTS("14002", "data not exists"),
    DATA_ALREADY_DEAL("14004", "数据已经处理"),
    INVITE_CODE_ERROR("15000", "invite code error"),
    USER_NOT_EXISTS("15001", "user not exists"),
    USER_EXISTS("15002", "user exists"),
    USER_LOCKED("15007", "User locked"),
    USER_PASSWORD_ERROR("15009", "user password error"),
    USER_SECRET_PASSWORD_NOT_SET("15010", "user secret password not set"),
    USER_SECRET_PASSWORD_ERROR("15011", "user secret password error"),
    USER_PASSWORD_INVALID("15031", "用户密码格式不合法"),
    USER_OR_PASSWORD_ERROR("15032", "账号或密码不正确"),
    USER_LOGIN_KICK_OUT("15051", "user login kick out"),
    USER_ACCOUNT_RULE_ERROR("15060", "用户账户格式有误"),
    USER_OLD_PASSWORD_ERROR("21003", "user old password error"),
    BALANCE_NOT_ENOUGH("22007", "balance not enough"),
    CHANGE_AMOUNT_IS_LESS_ZERO("22033", "CHANGE AMOUNT IS LESS ZERO"),


    INCOME_ADDRESS_NOT_EXIST("49001", "income address not exist"),
    CREATE_TX_PO_NOT_EXIST("49002", "tron create tx po not exist"),
    UPDATE_INCOME_ADDRESS_FAILED("49004", "update income address failed"),
    TRC20_TX_SAVE_FAILED("49005", "trc20 tx save failed"),
    TRX_BALANCE_NOT_ENOUGH("49006", "trx balance not enough"),
    TOKEN_BALANCE_NOT_ENOUGH("49007", "token balance not enough"),
    INCOME_ADDRESS_NO_PRIVATE_KEY("49008", "income address not config private key"),
    INCOME_ADDRESS_NOT_BASE58("49009", "income address not tron's base58 address"),

    BNB_BALANCE_NOT_ENOUGH("49010", "bnb balance not enough"),

    ADDRESS_TYPE_ILLEGAL("49011", "address type is illegal"),

    INCOME_ADDRESS_NOT_HEX("49012", "income address not bsc's hex address"),

    PARAM_ERROR_DESC("14006", "参数[{0}]错误"),

    WALLET_ADDRESS_ERROR("30001", "钱包地址错误"),

    WITHDRAW_AMOUNT_SMALL_THAN_FEE("40001", "提币金额小于等于手续费"),
    WITHDRAW_AMOUNT_MUST_IN_RANGE("40002", "提币金额必须在区间{0}-{1}"),
    WITHDRAW_NOT_SELF("40003", "禁止自提币"),

    PERIOD_HOUR_ILLEGAL("40004", "小时周期，小数位精度为1"),

    PERIOD_HOUR_MINIMUM("40005", "小时周期，最小为0.2"),

    WEBSOCKET_SESSION_NOT_EXISTS("50001", "session not exists"),

    USER_CARD_EXISTS("U801", "用户收款信息已存在"),
    USER_CARD_NOT_EXISTS("U802", "用户未设置收款信息"),
    USER_CARD_MORE("U803", "银行卡数量超限"),

    SERVICE_TIMEOUT("S1001", "服务已到期"),

    ORDER_BUY_LIMIT("T1001", "每日預購最多{0}單"),

    NEED_BIND_GOOGLE_CODE("C8901", "未绑定谷歌验证码"),

    ACCOUNT_NOT_EXIST("A101", "账户不存在"),
    ACCOUNT_CONCURRENT("A102", "操作异常，请重试"),
    ACCOUNT_ALLOWANCE_TOO_LOW("A103", "设置失败，该用户已经使用了{0}"),
    ACCOUNT_BALANCE_INSUFFICIENT("A104", "余额不足"),

    ACCOUNT_FREEZE_NOT_FOUND("A105", "找不到冻结记录"),
    ACCOUNT_FREEZE_INSUFFICIENT("A106", "冻结余额不足"),
    ACCOUNT_BALANCE_INSUFFICIENT_OR_CONCURRENT("A104", "余额不足或者并发问题，请重试"),

    CURRENCY_NOT_FOUND("C101", "币种不支持"),

    ;


    /**
     * 枚举编码
     */
    private String code;

    /**
     * 描述说明
     */
    private String desc;

    BizErrCode(String code, String desc) {
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
