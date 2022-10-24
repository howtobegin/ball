package com.ball.biz.bet.order.settle.analyze;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.OddsType;
import com.ball.biz.bet.order.OrderHelper;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.order.settle.assist.OverUnderAssist;
import com.ball.biz.bet.order.settle.parse.ParserHolder;
import com.ball.biz.bet.order.settle.parse.bo.HandicapParam;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.order.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 让球
 *
 * 影响因素：oddsType
 *
 * 例如主让客
 * 前提条件：
 *
 * 全投：拜仁慕尼黑 3，即拜仁慕尼黑让3个求给皮尔森，也就是说拜仁慕尼黑的净胜球要>3个球才算赢
 * 当：
 * 净胜球>3个时，全赢
 * 净胜球=3个时，不赢不输
 * 净胜球<3个时，投注金额全输
 * 均投：拜仁慕尼黑 3/3.5，即投注金额平均分配投入到这两项
 * 当：
 * 净胜球<3个时，投注金额全输
 * 净胜球=3个时，输一半，分解来看，即3不输不赢，3.5为输，所以输一半
 * 净胜球>3个时，全赢，净胜球大于3个时，则只有4个以上，同时满足>3和>3.5，全赢
 * 另一种情况，拜仁慕尼黑3.5/4
 * 当 净胜球=4个时，赢一半，即满足>3.5，赢,=4，不输不赢
 * 所以让球基本结果分为以下几种：全赢、全输、不赢不输、赢一半、输一半
 *
 * @author lhl
 * @date 2022/10/22 下午9:43
 */
@Slf4j
@Component
public class HandicapAnalyzer extends AbstractAnalyzer {
    protected static final String AWAY_HANDICAP_HOME_FLAG = "-";

    @Override
    protected BetResult doAnalyze(OrderInfo order, Schedules schedules) {
        // 最后比赛得分
        Integer homeLastScore = getHomeLastScore(schedules);
        Integer awayLastScore = getAwayLastScore(schedules);
        log.info("orderId {} matchId {} homeLastScore {} awayLastScore {}",order.getOrderId(), order.getMatchId(), homeLastScore,awayLastScore);
        // 用户投注盘口
        OddsData oddsData = OrderHelper.parse(order.getOddsData(), OddsData.class);
        // 赔率类型
        OddsType oddsType = OddsType.parse(oddsData.getOddsType());
        log.info("oddsType {}", oddsType);
        if (OddsType.NONE == oddsType) {
            return BetResult.UNSETTLED;
        }

        // 投注时得分
        Integer homeCurrentScore = order.getHomeCurrentScore();
        Integer awayCurrentScore = order.getAwayCurrentScore();
        // 如果是早盘和赛前即时盘，投注时比分按0:0算
        if (OddsType.EARLY_ODDS == oddsType || OddsType.INSTANT_ODDS == oddsType) {
            if (homeCurrentScore != 0) {
                log.warn("homeCurrentScore {} oddsType {}", homeCurrentScore, oddsType);
            }
            if (homeCurrentScore != 0) {
                log.warn("awayCurrentScore {} oddsType {}", awayCurrentScore, oddsType);
            }
            homeCurrentScore = 0;
            awayCurrentScore = 0;
        }
        // 净胜球
        int homeGoalDifference = (homeLastScore - homeCurrentScore) - (awayLastScore - awayCurrentScore);
        int awayGoalDifference = - homeGoalDifference;
        log.info("homeGoalDifference {} awayGoalDifference {}", homeGoalDifference, awayGoalDifference);
        // 即时盘口
        String instantHandicap = oddsData.getInstantHandicap();

        BizAssert.hasText(instantHandicap, BizErrCode.PARAM_ERROR_DESC,"instantHandicap");
        // 正数，主让客；负数（包括-0），客让主
        // 是否主队让球
        boolean homeHandicap = !instantHandicap.startsWith(AWAY_HANDICAP_HOME_FLAG);
        Handicap handicap = OverUnderAssist.analyzeHandicap(new BigDecimal(instantHandicap).abs());
        HandicapParam parseParam = HandicapParam.builder()
                .handicap(handicap)
                // 用户投注对象
                .betOption(order.getBetOption())
                .homeHandicap(homeHandicap)
                .homeGoalDifference(homeGoalDifference)
                .awayGoalDifference(awayGoalDifference)
                .build();
        BetResult betResult = ParserHolder.get(getHandicapType(), handicap.getBetType()).parse(parseParam);
        return betResult;
    }

    protected Integer getHomeLastScore(Schedules schedules) {
        return schedules.getHomeScore();
    }

    protected Integer getAwayLastScore(Schedules schedules) {
        return schedules.getAwayScore();
    }

    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP;
    }
}
