package com.ball.biz.bet.processor;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.order.entity.OrderInfo;

/**
 * @author lhl
 * @date 2022/10/22 上午11:05
 */
public interface BetProcessor {

    HandicapType getHandicapType();


    void betCheck(BetBo bo);

    /**
     * 投注
     * @param bo
     * @return
     */
    OrderInfo bet(BetBo bo);
}
