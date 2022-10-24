package com.ball.biz.bet.processor;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.match.entity.Odds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/22 上午11:27
 */
@Slf4j
@Component
public class HandicapBetProcessor extends AbstractBetProcessor {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP;
    }

    @Override
    protected String getBetOdds(Odds odds, BetBo bo) {
        return BetOption.HOME == bo.getBetOption() ? odds.getInstantHome() : odds.getInstantAway();
    }
}
