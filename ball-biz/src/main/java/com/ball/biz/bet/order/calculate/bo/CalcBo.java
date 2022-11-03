package com.ball.biz.bet.order.calculate.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/11/2 下午6:44
 */
@Getter
@Setter
@Builder
public class CalcBo {
    private Long userId;

    private BigDecimal betAmount;

    private BigDecimal odds;

    private String handicapType;

    private Integer oddsType;
}
