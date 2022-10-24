package com.ball.biz.bet.order.settle.parse.handicap;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.settle.parse.bo.HandicapParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/23 上午8:57
 */
@Slf4j
@Component
public class HandicapBetAllParser extends HandicapParser {
    @Override
    public BetType getBetType() {
        return BetType.ALL;
    }

    @Override
    public BetResult parse(HandicapParam param) {
        Handicap handicap = param.getHandicap();
        int homeGoalDifference = param.getHomeGoalDifference();
        int awayGoalDifference = param.getAwayGoalDifference();
        // 投主队还是客队
        boolean betHome = BetOption.HOME.isMe(param.getBetOption());

        BetResult betResult;
        // 如果主队让球
        if (param.isHomeHandicap()) {
            // 主队净胜球=4，盘口让球=3或3.5
            if (homeGoalDifference > handicap.getBig()) {
                betResult = betHome ? BetResult.WIN : BetResult.LOSE;
            } else if (homeGoalDifference == handicap.getBig()) {
                betResult = BetResult.DRAW;
            } else {
                betResult = betHome ? BetResult.LOSE : BetResult.WIN;
            }
        }
        // 客队让球
        else {
            // 客队净胜球=4，盘口让球=3或3.5
            if (awayGoalDifference > handicap.getBig()) {
                betResult = betHome ? BetResult.LOSE : BetResult.WIN;
            } else if (awayGoalDifference == handicap.getBig()) {
                betResult = BetResult.DRAW;
            } else {
                betResult = betHome ? BetResult.WIN : BetResult.LOSE;
            }
        }
        return betResult;
    }
}
