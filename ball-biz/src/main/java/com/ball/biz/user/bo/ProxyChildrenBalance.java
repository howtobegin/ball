package com.ball.biz.user.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-11-04 17:58
 */
@Setter
@Getter
public class ProxyChildrenBalance {

    private Integer status;

    private BigDecimal allowance;

    private BigDecimal balance;

    private BigDecimal freeze;
}
