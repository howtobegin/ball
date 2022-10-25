package com.ball.app.controller.user.vo;

import com.ball.base.util.AesPeculiarUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author littlehow
 */
@Setter
@Getter
public class ServiceReq {
    @NotBlank
    private String code;

    @NotBlank
    private String time;

    @NotNull
    private Integer operation;

    public void valid() {
        long distance = 1000L * 60 * 60 * 48;
        String result = AesPeculiarUtil.decode(code, "svr");
        long ss = Long.parseLong(time);
        long now = System.currentTimeMillis();
        BizAssert.isTrue(ss > now - distance && ss < now + distance, BizErrCode.PARAM_ERROR_DESC, code);
        BizAssert.isTrue(result.equals("fbi-" + time), BizErrCode.PARAM_ERROR_DESC, code);
        BizAssert.isTrue(operation == 0 || operation == 1, BizErrCode.PARAM_ERROR_DESC, operation.toString());
    }
}
