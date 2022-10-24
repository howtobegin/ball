package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
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
public class HandicapMatchOddsResp {
    private LeagueResp league;

    private List<MatchOddsResp> matchOddsResp;
}
