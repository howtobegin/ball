package com.ball.biz.bet.order.settle.parse.handicap;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.settle.parse.bo.HandicapParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 1.5/2
 *
 * @author lhl
 * @date 2022/10/23 上午9:00
 */
@Slf4j
@Component
public class HandicapBetAvgHalfOneParser extends HandicapParser {
    @Override
    public BetType getBetType() {
        return BetType.AVG_HALF_ONE;
    }

    @Override
    public BetResult parse(HandicapParam param) {
        String betOption = param.getBetOption();
        boolean homeHandicap = param.isHomeHandicap();
        Handicap handicap = param.getHandicap();
        int homeGoalDifference = param.getHomeGoalDifference();
        int awayGoalDifference = param.getAwayGoalDifference();
        // 投主队还是客队
        boolean betHome = BetOption.HOME.isMe(betOption);

        BetResult betResult;
        if (homeHandicap) {
            if (homeGoalDifference > handicap.getBig()) {
                betResult = betHome ? BetResult.WIN : BetResult.LOSE;
            } else if (homeGoalDifference == handicap.getBig()) {
                betResult = betHome ? BetResult.WIN_HALF : BetResult.LOSE_HALF;
            } else {
                betResult = betHome ? BetResult.LOSE : BetResult.WIN;
            }
        } else {
            if (awayGoalDifference > handicap.getBig()) {
                betResult = betHome ? BetResult.LOSE : BetResult.WIN;
            } else if (awayGoalDifference == handicap.getBig()) {
                betResult = betHome ? BetResult.LOSE_HALF : BetResult.WIN_HALF;
            } else {
                betResult = betHome ? BetResult.WIN : BetResult.LOSE;
            }
        }
        return betResult;
    }
}
