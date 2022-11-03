package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-11-03 15:39
 */
@Setter
@Getter
public class ProxyRateResp {
    @ApiModelProperty("代理编号")
    private Long proxyUid;

    @ApiModelProperty("代理分成")
    private BigDecimal proxyRate;

    @ApiModelProperty("总代理分成")
    private BigDecimal totalProxyRate;
}
