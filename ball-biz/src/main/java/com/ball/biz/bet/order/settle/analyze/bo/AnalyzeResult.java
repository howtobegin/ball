package com.ball.biz.bet.order.settle.analyze.bo;

import com.ball.biz.bet.enums.BetResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/24 下午5:41
 */
@Getter
@Setter
public class AnalyzeResult {
    /**
     * 投注结果
     */
    private BetResult betResult;

    /**
     * 主队全场或半场得分
     */
    private Integer homeScore;

    /**
     * 客队全场或半场得分
     */
    private Integer awayScore;

    @Builder
    public AnalyzeResult(BetResult betResult, Integer homeScore, Integer awayScore) {
        this.betResult = betResult;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
}
