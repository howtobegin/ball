package com.ball.biz.exception;


import com.ball.base.exception.BizErr;
import com.ball.base.exception.IBizErrCode;

public class BizException extends BizErr {

    public BizException(IBizErrCode message, String ...argus) {
        super(message, false, argus);
    }

}
