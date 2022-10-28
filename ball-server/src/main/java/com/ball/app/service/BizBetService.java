package com.ball.app.service;

import com.ball.biz.annotation.Idempotent;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.processor.BetProcessorHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/28 下午5:10
 */
@Slf4j
@Component
public class BizBetService {
    /**
     * 限制用户单场投注速度，1秒1次
     * @param bo
     */
    @Idempotent(key = "bet", subKey = "#bo.userNo + ':' + #bo.matchId", time = 1000)
    public void bet(BetBo bo) {
        BetProcessorHolder.get(bo.getHandicapType()).bet(bo);
    }
}
