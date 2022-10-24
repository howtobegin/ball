package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.MatchResult;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.order.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 独赢
 * 条件：比赛完成
 * 结果：只有输赢
 *
 * @author lhl
 * @date 2022/10/21 上午10:26
 */
@Slf4j
@Component
public class EuropeOddsAnalyzer extends AbstractAnalyzer {
    @Override
    protected BetResult doAnalyze(OrderInfo order, Schedules schedules) {
        // 比赛结果
        MatchResult matchResult = MatchResult.parse(schedules.getHomeScore(), schedules.getAwayScore());
        // 用户投注
        String betTeam = order.getBetOption();
        // 投中赢，否则输
        return matchResult.name().equalsIgnoreCase(betTeam) ? BetResult.WIN : BetResult.LOSE;
    }

    @Override
    public HandicapType getHandicapType() {
        return HandicapType.EUROPE_ODDS;
    }
}
