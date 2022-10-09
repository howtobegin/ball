package com.ball.base.exception;


public class AssertException extends BizErr {

    public AssertException(IBizErrCode message, String ...argus) {
        super(message, false, argus);
    }

}
