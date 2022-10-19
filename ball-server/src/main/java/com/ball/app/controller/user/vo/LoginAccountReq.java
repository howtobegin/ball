package com.ball.app.controller.user.vo;

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
}
