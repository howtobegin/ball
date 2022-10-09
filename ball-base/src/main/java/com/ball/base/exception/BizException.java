package com.ball.base.exception;

public class BizException extends RuntimeException {

    public BizException(String msg) {
        this(msg, false);
    }

    public BizException(String msg, boolean writableStack) {
        super(msg, null, true, writableStack);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
