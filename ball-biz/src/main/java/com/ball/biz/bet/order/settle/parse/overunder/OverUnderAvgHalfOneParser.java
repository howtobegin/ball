package com.ball.biz.bet.order.settle.parse.overunder;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.settle.parse.bo.OverUnderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/24 上午10:37
 */
@Slf4j
@Component
public class OverUnderAvgHalfOneParser extends OverUnderParser {
    @Override
    public BetType getBetType() {
        return BetType.AVG_HALF_ONE;
    }

    @Override
    public BetResult parse(OverUnderParam param) {
        int totalScore = param.getTotalScore();
        Handicap handicap = param.getHandicap();
        BetOption betOption = param.getBetOption();

        BetResult betResult;
        if (totalScore > handicap.getBig()) {
            betResult = betOption == BetOption.OVER ? BetResult.WIN : BetResult.LOSE;
        } else if (totalScore == handicap.getBig()) {
            betResult = betOption == BetOption.OVER ? BetResult.WIN_HALF : BetResult.LOSE_HALF;
        } else {
            betResult = betOption == BetOption.OVER ? BetResult.LOSE : BetResult.WIN;
        }
        return betResult;
    }
}
