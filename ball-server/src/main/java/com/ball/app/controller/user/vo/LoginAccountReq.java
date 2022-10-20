package com.ball.app.controller.user.vo;

import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author littlehow
 */
@Setter
@Getter
public class LoginAccountReq {
    @ApiModelProperty(value = "登入账号", required = true)
    @NotBlank(message = "loginAccount must be not null")
    private String loginAccount;

    public void valid() {
        BizAssert.isTrue(loginAccount.length() > 5 && loginAccount.length() < 13, BizErrCode.USER_ACCOUNT_RULE_ERROR);
        String t1 = loginAccount.replaceAll("[a-zA-Z]", "");
        BizAssert.isTrue(loginAccount.length() - 2 >= t1.length(), BizErrCode.USER_ACCOUNT_RULE_ERROR);
        t1 = t1.replaceAll("\\d", "");
        BizAssert.isTrue(t1.length() == 0, BizErrCode.USER_ACCOUNT_RULE_ERROR);
    }
}
