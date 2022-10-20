package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author littlehow
 */
@Setter
@Getter
public class LoginReq {
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "account must be not null")
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "password must be not null")
    private String password;

    @ApiModelProperty("安全码,存在就需要传")
    private String secretPassword;

    @ApiModelProperty(value = "代理类型 1:登1 2:登2 3:登3", required = true)
    @NotNull
    @Range(min = 1, max = 3)
    private Integer userType;
}
