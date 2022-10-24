package com.ball.biz.bet.order.settle.parse;

import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.enums.HandicapType;

/**
 * 计算输赢结果，不涉及amont
 * @author lhl
 * @date 2022/10/23 上午8:53
 */
public interface Parser<T> {
    HandicapType getHandicapType();

    BetType getBetType();

    BetResult parse(T param);
}
