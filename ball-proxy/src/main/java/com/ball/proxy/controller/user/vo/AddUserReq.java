package com.ball.proxy.controller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author littlehow
 */
@Setter
@Getter
public class AddUserReq {
    @ApiModelProperty("代理商 登1或登2为登3添加会员时需要")
    private Long proxyUid;

    @ApiModelProperty(value = "盘口种类 A B C D", required = true)
    @NotBlank(message = "handicap must be not null")
    private String handicap;

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "account must be not null")
    private String account;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "userName must be not null")
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "password must be not null")
    private String password;

    @ApiModelProperty(value = "信用额度", required = true)
    @NotBlank(message = "amount must be not null")
    @Min(1L)
    private Long amount;

    @ApiModelProperty(value = "币种", required = true)
    @NotBlank(message = "currency must be not null")
    private String currency;
}
