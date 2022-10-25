package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.MatchTimeType;
import com.ball.biz.bet.order.OrderHelper;
import com.ball.biz.bet.order.bo.OddsScoreData;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.order.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 波胆
 * @author lhl
 * @date 2022/10/22 上午9:33
 */
@Slf4j
@Component
public class CorrectScoreAnalyzer extends AbstractAnalyzer {
    @Override
    protected AnalyzeResult doAnalyze(OrderInfo order, Schedules schedules) {
        // 最后比分
        Integer homeScore = getHomeLastScore(schedules);
        Integer awayScore = getAwayLastScore(schedules);
        log.info("orderId {} matchId {} homeScore {} awayScore {}", order.getOrderId(),order.getMatchId(),homeScore,awayScore);
        BetResult betResult;
        // 用户投注的比分
        OddsScoreData oddsScoreData = OrderHelper.parse(order.getOddsData(), OddsScoreData.class);
        // 如果是投注具体比分，和最后比分比较即可，
        if (BetOption.SCORE.isMe(order.getBetOption())) {
            log.info("betScore homeScore {} awayScore {}", oddsScoreData.getHomeScore(), oddsScoreData.getAwayScore());
            betResult = homeScore.equals(oddsScoreData.getHomeScore()) && awayScore.equals(oddsScoreData.getAwayScore()) ?
                    BetResult.WIN : BetResult.LOSE;
        } else if (BetOption.SCORE_OTHER.isMe(order.getBetOption())) {
            boolean other = true;
            // 如果投注的是其他比分，需要判定比赛比分是否有对应投注项
            for (OddsScoreData.OddsScoreItem item : oddsScoreData.getOtherItems()) {
                log.info("allScore type {} homeScore {} awayScore {}", item.getType(), item.getHomeScore(), item.getAwayScore());
                // 下单的时候已过滤整场或半场，这里再过滤一次，确保万无一失
                if (!getMatchTimeType().isMe(item.getType())) {
                    continue;
                }
                if (homeScore.equals(item.getHomeScore()) && awayScore.equals(item.getAwayScore())) {
                    other = false;
                    break;
                }
            }
            betResult = other ? BetResult.WIN : BetResult.LOSE;
        } else {
            throw new BizException(BizErrCode.NOT_FOUND_BET_OPTION);
        }
        log.info("orderId {} HandicapType {} lastScore {}:{} betOption {} betScore {}:{} betResult {}",order.getOrderId(),getHandicapType(), homeScore,awayScore,order.getBetOption(), oddsScoreData.getHomeScore(), oddsScoreData.getAwayScore(), betResult);
        return AnalyzeResult.builder()
                .betResult(betResult)
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();
    }

    protected MatchTimeType getMatchTimeType() {
        return MatchTimeType.FULL;
    }

    protected Integer getHomeLastScore(Schedules schedules) {
        return schedules.getHomeScore();
    }

    protected Integer getAwayLastScore(Schedules schedules) {
        return schedules.getAwayScore();
    }

    @Override
    public HandicapType getHandicapType() {
        return HandicapType.CORRECT_SCORE;
    }
}
