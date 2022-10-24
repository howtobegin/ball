package com.ball.biz.bet.order.settle.parse.bo;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.order.bo.Handicap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/24 上午10:31
 */
@Getter
@Setter
public class OverUnderParam {
    private int totalScore;
    private BetOption betOption;
    private Handicap handicap;

    @Builder
    public OverUnderParam(int totalScore, BetOption betOption, Handicap handicap) {
        this.totalScore = totalScore;
        this.betOption = betOption;
        this.handicap = handicap;
    }
}
