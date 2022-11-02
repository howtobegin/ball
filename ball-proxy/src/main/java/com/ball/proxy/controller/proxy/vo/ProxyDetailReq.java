package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JimChery
 * @since 2022-11-02 14:31
 */
@Setter
@Getter
public class ProxyDetailReq {
    @ApiModelProperty(value = "代理用户编号")
    private Long proxyUid;

    public boolean hasProxyUid() {
        return proxyUid != null;
    }
}
