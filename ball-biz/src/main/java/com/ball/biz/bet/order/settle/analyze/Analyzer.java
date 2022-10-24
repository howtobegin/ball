package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.order.entity.OrderInfo;

/**
 * @author lhl
 * @date 2022/10/20 下午7:25
 */
public interface Analyzer {
    HandicapType getHandicapType();

    BetResult settleBetResult(OrderInfo order);
}
