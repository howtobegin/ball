package com.ball.proxy.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author JimChery
 * @since 2022-11-07 16:02
 */
@Setter
@Getter
public class UpdatePasswordReq extends UserNoReq {
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "password must be not null")
    private String password;
}
