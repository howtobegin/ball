package com.ball.app.controller.match.vo;

import com.ball.biz.bet.enums.HandicapType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/20 下午3:24
 */
@Getter
@Setter
@ApiModel("比赛指数信息")
public class MatchOddsResp {

    @ApiModelProperty("比赛ID")
    private MatchResp match;

    @ApiModelProperty("玩儿法总数")
    private Integer count;

    @ApiModelProperty("全场指数信息key:HANDICAP[让球全场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场] ")
    private Map<HandicapType, List<OddsResp>> odds;

    @ApiModelProperty("半场指数信息key:HANDICAP_HALF[让球半场],OVER_UNDER_HALF[大小球半场] ")
    private Map<HandicapType, List<OddsResp>> halfOdds;

    @ApiModelProperty("波胆指数信息key:CORRECT_SCORE[波胆全场],CORRECT_SCORE_HALL[波胆半场] ")
    private Map<HandicapType, List<OddsScoreResp>> oddsScore;

    @Builder
    public MatchOddsResp(MatchResp match, Integer count, Map<HandicapType, List<OddsResp>> odds, Map<HandicapType, List<OddsResp>> halfOdds, Map<HandicapType, List<OddsScoreResp>> oddsScore) {
        this.match = match;
        this.count = count;
        this.odds = odds;
        this.halfOdds = halfOdds;
        this.oddsScore = oddsScore;
    }
}
