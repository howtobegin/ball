package com.ball.biz.bet.processor;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.match.entity.Odds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/22 上午11:35
 */
@Slf4j
@Component
public class OverUnderBetProcessor extends AbstractBetProcessor {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.OVER_UNDER;
    }

    @Override
    protected String getBetOdds(Odds odds, BetBo bo) {
        return BetOption.OVER == bo.getBetOption() ? odds.getInstantOver() : odds.getInstantUnder();
    }
}
