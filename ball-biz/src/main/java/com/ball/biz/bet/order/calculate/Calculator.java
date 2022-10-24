package com.ball.biz.bet.order.calculate;

import com.ball.biz.bet.order.calculate.bo.CalcResult;
import com.ball.biz.bet.enums.BetResult;

import java.math.BigDecimal;

/**
 * 计算器
 * 计算结果：比赛ID、盘口类型、handicap_index
 * 计算结果金额：
 * @author lhl
 * @date 2022/10/18 下午6:45
 */
public interface Calculator {
    BetResult getBetResult();

    CalcResult calc(BigDecimal betAmount, BigDecimal odds);
}
