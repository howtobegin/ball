package com.ball.proxy.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-11-07 16:25
 */
@Setter
@Getter
public class UpdateProxyInfo extends UpdateUserInfo {
    @ApiModelProperty("代理商分成数")
    private BigDecimal proxyRate;

    public boolean invalid() {
        return super.invalid() && proxyRate == null;
    }

    public boolean hasProxyRate() {
        return proxyRate != null && proxyRate.compareTo(BigDecimal.ZERO) >= 0;
    }
}
