package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-11-04 18:08
 */
@Setter
@Getter
@Accessors(chain = true)
public class ProxyChildrenResp {
    @ApiModelProperty("状态 0:停用 1:启用")
    private Integer status;

    @ApiModelProperty("会员数")
    private Integer userCount;

    @ApiModelProperty("信用额度")
    private BigDecimal balance;
}
