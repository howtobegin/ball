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

    /**
     * 在finally里面BetCache.clear()
     * @param bo
     * @param checkUser
     */
    void betCheck(BetBo bo, boolean checkUser);

    /**
     * 投注
     * @param bo
     * @return
     */
    OrderInfo bet(BetBo bo);
}
