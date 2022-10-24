package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.order.settle.assist.OverUnderAssist;
import com.ball.biz.bet.order.settle.parse.ParserHolder;
import com.ball.biz.bet.order.settle.parse.bo.OverUnderParam;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.order.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.ball.biz.bet.order.OrderHelper.parse;

/**
 * 大小
 * 条件：比赛完成
 * 结果：全赢，赢一半，不输不赢，全输，输一半
 * 例子：
 * 全投：大 4
 * 当
 * 总进球数 >4 时，全赢
 * 总进球数 =4 时，不输不赢
 * 总进球数 <4 时，全输
 * 均投：大 4/4.5
 * 当
 * 总进球数 >4时，全赢
 * 总进球数 =4时，输一半
 * 总进球数 <4时，全输
 * 另一种情况，大 4.5/5
 * 总进球数 =5时，赢一半
 *
 * @author lhl
 * @date 2022/10/21 上午11:06
 */
@Slf4j
@Component
public class OverUnderAnalyzer extends AbstractAnalyzer {
    @Override
    protected BetResult doAnalyze(OrderInfo order, Schedules schedules) {
        // 总分
        int totalScore = getTotalScore(schedules);
        // 用户投注
        OddsData oddsData = parse(order.getOddsData(), OddsData.class);
        // 解析投注盘口
        Handicap handicap = OverUnderAssist.analyzeHandicap(oddsData.getInstantHandicap());
        BetOption betOption = BetOption.valueOf(order.getBetOption());

        OverUnderParam parseParam = OverUnderParam.builder()
                .totalScore(totalScore)
                .betOption(betOption)
                .handicap(handicap)
                .build();
        return ParserHolder.get(getHandicapType(), handicap.getBetType()).parse(parseParam);
    }

    protected Integer getTotalScore(Schedules schedules) {
        return schedules.getHomeScore() + schedules.getAwayScore();
    }

    @Override
    public HandicapType getHandicapType() {
        return HandicapType.OVER_UNDER;
    }

}
