package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/11/15 下午5:45
 */
@Getter
@Setter
@ApiModel("世界杯小组玩法返回信息")
public class OutrightsGroupOddsResp {

    @ApiModelProperty("联赛名称(中文)")
    private String leagueNameZh;

    @ApiModelProperty("球队/国家/球员 ID")
    private String itemId;

    @ApiModelProperty("球队/国家/球员中文名")
    private String itemZh;

    @ApiModelProperty("比赛")
    private Integer gamesPlayed;

    @ApiModelProperty("积分")
    private Integer points;

    @ApiModelProperty("净胜")
    private Integer goalsDifference;

    @ApiModelProperty("冠军")
    private OutrightsOddsResp winnerOdds;

    @ApiModelProperty("首两名")
    private OutrightsOddsResp qualifiedOdds;

    @ApiModelProperty("胜")
    private Integer win;

    @ApiModelProperty("平")
    private Integer drawn;

    @ApiModelProperty("负")
    private Integer lose;

    @ApiModelProperty("进球")
    private Integer goalsScored;

    @ApiModelProperty("失球")
    private Integer goalsAgainst;

    @Builder
    public OutrightsGroupOddsResp(String leagueNameZh, String itemId, String itemZh, Integer gamesPlayed, Integer points, Integer goalsDifference, OutrightsOddsResp winnerOdds, OutrightsOddsResp qualifiedOdds, Integer win, Integer drawn, Integer lose, Integer goalsScored, Integer goalsAgainst) {
        this.leagueNameZh = leagueNameZh;
        this.itemId = itemId;
        this.itemZh = itemZh;
        this.gamesPlayed = gamesPlayed;
        this.points = points;
        this.goalsDifference = goalsDifference;
        this.winnerOdds = winnerOdds;
        this.qualifiedOdds = qualifiedOdds;
        this.win = win;
        this.drawn = drawn;
        this.lose = lose;
        this.goalsScored = goalsScored;
        this.goalsAgainst = goalsAgainst;
    }
}
