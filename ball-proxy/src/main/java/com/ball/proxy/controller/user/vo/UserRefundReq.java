package com.ball.proxy.controller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author JimChery
 * @since 2022-10-31 16:03
 */
@Setter
@Getter
public class UserRefundReq {
    @ApiModelProperty(value = "用户编号", required = true)
    @NotNull
    private Long userId;

    @ApiModelProperty(value = "退水限额配置项", required = true)
    @NotEmpty
    @Valid
    private List<UserRefundConfigReq> config;
}
