package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author littlehow
 */
@Setter
@Getter
public class AddProxyReq {
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "account must be not null")
    private String account;

    @ApiModelProperty(value = "代理用户名", required = true)
    @NotBlank(message = "userName must be not null")
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "password must be not null")
    private String password;

    @ApiModelProperty(value = "额度模式 RECOVERY:自动恢复 BALANCE:余额浮动", required = true)
    @NotBlank(message = "password must be not null")
    private String balanceMode;

    @ApiModelProperty(value = "信用额度", required = true)
    @NotNull(message = "amount must be not null")
    @Min(1L)
    private Long amount;

}
