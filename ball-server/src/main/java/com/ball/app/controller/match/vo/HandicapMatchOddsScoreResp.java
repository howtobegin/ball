package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/10/24 上午11:42
 */
@Getter
@Setter
@ApiModel("滚球比赛指数信息")
public class HandicapMatchOddsScoreResp {
    private LeagueResp league;

    private List<MatchOddsScoreResp> matchOddsScoreResp;

    @Builder
    public HandicapMatchOddsScoreResp(LeagueResp league, List<MatchOddsScoreResp> matchOddsScoreResp) {
        this.league = league;
        this.matchOddsScoreResp = matchOddsScoreResp;
    }
}
