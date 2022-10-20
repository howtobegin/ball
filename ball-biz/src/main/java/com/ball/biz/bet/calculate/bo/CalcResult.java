package com.ball.biz.bet.calculate.bo;

import com.ball.biz.bet.enums.BetResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/17 上午11:05
 */
@Getter
@Setter
@Builder
public class CalcResult {
    private BetResult result;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 赔率
     */
    private BigDecimal odds;

    /**
     * 包含本金的结果金额
     */
    private BigDecimal resultAmount;
}
