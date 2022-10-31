package com.ball.proxy.controller.proxy.vo;

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
public class ProxyRefundReq {
    @ApiModelProperty(value = "代理用户编号", required = true)
    @NotNull
    private Long proxyUid;

    @ApiModelProperty(value = "退水限额配置项", required = true)
    @NotEmpty
    @Valid
    private List<ProxyRefundConfigReq> config;
}
