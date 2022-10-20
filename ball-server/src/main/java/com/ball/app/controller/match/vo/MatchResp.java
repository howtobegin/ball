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
     * 1: League2: Cup
     */
    @ApiModelProperty("1: League2: Cup")
    private Integer leagueType;

    @ApiModelProperty("leagueId")
    private String leagueId;

    /**
     * Full name, e.g. Brazil Serie A
     */
    @ApiModelProperty("Full name, e.g. Brazil Serie A")
    private String leagueName;

    /**
     * Short name, e.g. BRA D1
     */
    @ApiModelProperty("Short name, e.g. BRA D1")
    private String leagueShortName;

    @ApiModelProperty("leagueColor")
    private String leagueColor;

    @ApiModelProperty("subLeagueId")
    private String subLeagueId;

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
    @ApiModelProperty("Unix timestamp")
    private Integer startTime;

    /**
     * The kick-off time of the first half or the second half, unix timestamp
     */
    @ApiModelProperty("The kick-off time of the first half or the second half, unix timestamp")
    private Integer halfStartTime;

    /**
     * 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
     */
    @ApiModelProperty("0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed")
    private Boolean status;

    @ApiModelProperty("homeId")
    private String homeId;

    @ApiModelProperty("homeName")
    private String homeName;

    @ApiModelProperty("awayId")
    private String awayId;

    @ApiModelProperty("awayName")
    private String awayName;

    @ApiModelProperty("homeScore")
    private Integer homeScore;

    @ApiModelProperty("awayScore")
    private Integer awayScore;

    /**
     * First half score, home team
     */
    @ApiModelProperty("")
    private Integer homeHalfScore;

    /**
     * First half score, away team
     */
    @ApiModelProperty("First half score, away team")
    private Integer awayHalfScore;

    @ApiModelProperty("homeRed")
    private Integer homeRed;

    @ApiModelProperty("awayRed")
    private Integer awayRed;

    @ApiModelProperty("homeYellow")
    private Integer homeYellow;

    @ApiModelProperty("awayYellow")
    private Integer awayYellow;

    @ApiModelProperty("homeCorner")
    private Integer homeCorner;

    @ApiModelProperty("awayCorner")
    private Integer awayCorner;

    /**
     * The ranking of the team in the league, home team
     */
    @ApiModelProperty("The ranking of the team in the league, home team")
    private String homeRank;

    /**
     * The ranking of the team in the league, away team
     */
    @ApiModelProperty("The ranking of the team in the league, away team")
    private String awayRank;

    /**
     * e.g. 2019-2020
     */
    @ApiModelProperty("e.g. 2019-2020")
    private String season;

    /**
     * Matches with [Interface Cup Stage Profile] stageId; To distinguish the different stages of the cup, the league does not return this data.
     */
    @ApiModelProperty("Matches with [Interface Cup Stage Profile] stageId; To distinguish the different stages of the cup, the league does not return this data.")
    private String stageId;

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
}
