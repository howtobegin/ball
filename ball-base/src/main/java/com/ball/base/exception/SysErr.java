package com.ball.base.exception;


import lombok.Data;

/**
 * 如果系统异常，统一使用这个类对外扔异常，BizErr是预期的业务异常，BizSysErr是非预期的系统异常，
 * 返回给前端是500(INTERNAL SERVER ERROR)状态码，比如数据库超时、外部服务调用超时，如果有资
 * 金操作，那么这个返回给调用方相当于结果不明确，需要重试
 */
@Data
public class SysErr extends RuntimeException {
    /**
     * 错误编码
     */
    private String code = "102";

    /**
     * 错误描述
     */
    private String desc = "System is busy, please try again later";

}
