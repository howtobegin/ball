package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fanyongpeng
 * @date 10/25/22
 **/
@Data
@ApiModel("赛程信息")
public class ScheduleDetailResp {
    private Long id;

    @ApiModelProperty("比赛id")
    private String matchId;

    /**
     * 1: League2: Cup
     */
    @ApiModelProperty("比赛类型， 1: League 2: Cup")
    private Integer leagueType;

    @ApiModelProperty("联赛id")
    private String leagueId;

    /**
     * Full name, e.g. Brazil Serie A
     */
    @ApiModelProperty("联赛名称")
    private String leagueName;

    /**
     * Short name, e.g. BRA D1
     */
    @ApiModelProperty("联赛简称")
    private String leagueShortName;

//    @ApiModelProperty("")
//    private String leagueColor;
//
//    @ApiModelProperty("")
//    private String subLeagueId;
//
//    /**
//     * The on-going sub league of the league, e.g. Western Paly Off
//     */
//    @ApiModelProperty("")
//    private String subLeagueName;

    @ApiModelProperty("比赛日期")
    private LocalDateTime matchDate;

    /**
     * Match scheduled time, unix timestamp
     */
    @ApiModelProperty("比赛时间")
    private Integer matchTime;

    /**
     * Unix timestamp
     */
    @ApiModelProperty("开始时间")
    private Integer startTime;

    /**
     * The kick-off time of the first half or the second half, unix timestamp
     */
//    @ApiModelProperty("半场开始时间 上半场或者下半场")
//    private Integer halfStartTime;

    /**
     * 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
     */
    @ApiModelProperty("比赛 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed")
    private Integer status;

    @ApiModelProperty("主队id")
    private String homeId;

    @ApiModelProperty("主队名称")
    private String homeName;

    @ApiModelProperty("客队id")
    private String awayId;

    @ApiModelProperty("客队名称")
    private String awayName;

    @ApiModelProperty("主队比分")
    private Integer homeScore;

    @ApiModelProperty("客队比分")
    private Integer awayScore;

//    /**
//     * First half score, home team
//     */
//    @ApiModelProperty("主队半场得分")
//    private Integer homeHalfScore;
//
//    /**
//     * First half score, away team
//     */
//    @ApiModelProperty("客队半场得分")
//    private Integer awayHalfScore;
//
//    private Integer homeRed;
//
//    private Integer awayRed;
//
//    private Integer homeYellow;
//
//    private Integer awayYellow;
//
//    private Integer homeCorner;
//
//    private Integer awayCorner;
//
//    /**
//     * The ranking of the team in the league, home team
//     */
//    @ApiModelProperty("主队排名")
//    private String homeRank;
//
//    /**
//     * The ranking of the team in the league, away team
//     */
//    @ApiModelProperty("客队排名2")
//    private String awayRank;
//
//    /**
//     * e.g. 2019-2020
//     */
//    @ApiModelProperty("赛季")
//    private String season;
//
//    /**
//     * Matches with [Interface Cup Stage Profile] stageId; To distinguish the different stages of the cup, the league does not return this data.
//     */
//    private String stageId;
//
//    /**
//     * League round or cup stage, e.g. 10
//     */
//    private String round;
//
//    /**
//     * Cup group, e.g. A
//     */
//    private String leagueGroup;
//
//    /**
//     * e.g. Camp Nou
//     */
//    @ApiModelProperty("地点")
//    private String location;
//
//    /**
//     * e.g. Clear
//     */
//    @ApiModelProperty("气候")
//    private String weather;
//
//    /**
//     * e.g. 14℃～15℃
//     */
//    @ApiModelProperty("温度")
//    private String temperature;
//
//    /**
//     * Special case description of the match, e.g. Match end up with [0-3], due to (Torpedo-MAZ Minsk) withdraw from the match
//     */
//
//    private String matchExplain;
//
//    /**
//     * Is there Lineup data?
//     */
//    private Boolean hasLineup;
//
//    /**
//     * Is it a neutral venue?
//     */
//    private Boolean neutral;
//
//    /**
//     * 1: Home kickoff 2: Away kickoff
//     */
//    private Integer exKickOff;
//
//    /**
//     * How many minutes does the match have in regular time?
//     */
//    private Integer exMinute;
//
//    /**
//     * Regular time score, home team
//     */
//    private Integer exHomeScore;
//
//    /**
//     * Regular time score, away team
//     */
//    private Integer exAwayScore;
//
//    /**
//     * 1: Normal matches extratime ends; 2: Special matches (e.g. beach football, indoor football) extratime ends; 3: The match in extra time
//     */
//    private Integer exExtraTimeStatus;
//
//    /**
//     * Extra time score, home team
//     */
//    private Integer exExtraHomeScore;
//
//    /**
//     * Extra time score, away team
//     */
//    private Integer exExtraAwayScore;
//
//    /**
//     * Penalty score, home team
//     */
//    private Integer exPenHomeScore;
//
//    /**
//     * Penalty score, away team
//     */
//    private Integer exPenAwayScore;
//
//    private Integer exTwoRoundsHomeScore;
//
//    private Integer exTwoRoundsAwayScore;

    /**
     * Winner of the match 1: Home 2: Away
     */
    @ApiModelProperty("赢家 1 主队 ， 2 客队")
    private Integer exWinner;
}
