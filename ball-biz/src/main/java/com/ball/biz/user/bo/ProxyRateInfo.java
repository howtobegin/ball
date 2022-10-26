package com.ball.biz.user.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-10-26 14:22
 */
@Setter
@Getter
public class ProxyRateInfo {
    /**
     * 一级代理分成比例
     */
    private BigDecimal proxyOneRate = BigDecimal.ZERO;

    /**
     * 二级代理分成比例
     */
    private BigDecimal proxyTwoRate = BigDecimal.ZERO;

    /**
     * 三级代理分成比例
     */
    private BigDecimal proxyThreeRate = BigDecimal.ZERO;
}
