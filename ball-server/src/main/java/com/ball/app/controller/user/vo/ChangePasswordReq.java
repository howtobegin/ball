package com.ball.app.controller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author JimChery
 */
@Setter
@Getter
public class ChangePasswordReq {
    @ApiModelProperty(value = "旧密码", required = true)
    @NotBlank(message = "oldPassword must be not null")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "newPassword must be not null")
    private String newPassword;
}
