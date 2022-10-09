package com.ball.base.exception;


public enum BaseErrCode implements IBizErrCode {

    KEY_INVALID("11001", "bind key invalid"),
    BLOCK_SYNC_ERROR("71001", "block sync error"),
    ARTICLE_NOT_FOUND("71002", "article not found"),

    AREA_LEVEL_NOT_FOUND("81001", "area level not found"),
    TYPE_ERROR("22006", "错误的类型:{0}"),
    ;




    /**
     * 枚举编码
     */
    private String code;

    /**
     * 描述说明
     */
    private String desc;

    BaseErrCode(String code, String desc) {
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
