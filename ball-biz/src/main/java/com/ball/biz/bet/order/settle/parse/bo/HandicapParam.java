package com.ball.biz.bet.order.settle.parse.bo;

import com.ball.biz.bet.order.bo.Handicap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/24 上午10:32
 */
@Getter
@Setter
public class HandicapParam {
    /**
     * 盘口信息
     */
    private Handicap handicap;
    /**
     * 用户投注选项
     */
    private String betOption;
    /**
     * true 主队让球；false 客队让球
     */
    private boolean homeHandicap;

    /**
     * 主队净胜球
     */
    private int homeGoalDifference;
    /**
     * 客队净胜球
     */
    private int awayGoalDifference;

    @Builder
    public HandicapParam(Handicap handicap, String betOption, boolean homeHandicap, int homeGoalDifference, int awayGoalDifference) {
        this.handicap = handicap;
        this.betOption = betOption;
        this.homeHandicap = homeHandicap;
        this.homeGoalDifference = homeGoalDifference;
        this.awayGoalDifference = awayGoalDifference;
    }
}
