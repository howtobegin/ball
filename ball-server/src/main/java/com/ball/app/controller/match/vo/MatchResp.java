package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 对应schedule
 * @author lhl
 * @date 2022/10/20 下午3:29
 */
@Getter
@Setter
@ApiModel("比赛信息")
public class MatchResp {
    @ApiModelProperty("比赛ID")
    private String matchId;

    /**
     * Full name, e.g. Brazil Serie A
     */
    @ApiModelProperty("联赛名")
    private String leagueName;

    /**
     * Short name, e.g. BRA D1
     */
    @ApiModelProperty("Short name, e.g. BRA D1")
    private String leagueShortName;

    @ApiModelProperty("leagueColor")
    private String leagueColor;

    /**
     * The on-going sub league of the league, e.g. Western Paly Off
     */
    @ApiModelProperty("The on-going sub league of the league, e.g. Western Paly Off")
    private String subLeagueName;

    /**
     * Match scheduled time, unix timestamp
     */
    @ApiModelProperty("Match scheduled time, unix timestamp")
    private Integer matchTime;

    /**
     * Unix timestamp
     */
    @ApiModelProperty("比赛开始时间")
    private Integer startTime;

    /**
     * The kick-off time of the first half or the second half, unix timestamp
     */
    @ApiModelProperty("上半场或下半场开球时间，unix时间戳")
    private Integer halfStartTime;

    /**
     * 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
     */
    @ApiModelProperty("0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed")
    private Integer status;

    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("homeId")
    private String homeId;

    @ApiModelProperty("主队名")
    private String homeName;

    @ApiModelProperty("主队中文名")
    private String homeNameZh;

    @ApiModelProperty("awayId")
    private String awayId;

    @ApiModelProperty("客队名")
    private String awayName;

    @ApiModelProperty("客队中文名")
    private String awayNameZh;

    @ApiModelProperty("homeScore")
    private Integer homeScore;

    @ApiModelProperty("awayScore")
    private Integer awayScore;

    /**
     * First half score, home team
     */
    @ApiModelProperty("上半场主队得分")
    private Integer homeHalfScore;

    /**
     * First half score, away team
     */
    @ApiModelProperty("上半场客队得分")
    private Integer awayHalfScore;

    /**
     * League round or cup stage, e.g. 10
     */
    @ApiModelProperty("League round or cup stage, e.g. 10")
    private String round;

    /**
     * Cup group, e.g. A
     */
    @ApiModelProperty("Cup group, e.g. A")
    private String group;

    /**
     * e.g. Camp Nou
     */
    @ApiModelProperty("e.g. Camp Nou")
    private String location;

    @ApiModelProperty("是否收藏")
    private boolean isFavorite;
}
