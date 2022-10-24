package com.ball.biz.bet.order.calculate.bo;

import com.ball.biz.bet.enums.BetResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/17 上午11:14
 */
@Getter
@Setter
@Builder
public class PredictResult {
    /**
     * 输赢选项
     */
    private BetResult betResult;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 预测结果金额
     */
    private BigDecimal predictAmount;
}
