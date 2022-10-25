package com.ball.biz.match.entity;

import com.ball.base.model.Paging;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_schedules")
public class Schedules extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String matchId;

    /**
     * 1: League2: Cup
     */
    private Integer leagueType;

    private String leagueId;

    /**
     * Full name, e.g. Brazil Serie A
     */
    private String leagueName;

    /**
     * Short name, e.g. BRA D1
     */
    private String leagueShortName;

    private String leagueColor;

    private String subLeagueId;

    /**
     * The on-going sub league of the league, e.g. Western Paly Off
     */
    private String subLeagueName;

    private LocalDateTime matchDate;

    /**
     * Match scheduled time, unix timestamp
     */
    private Integer matchTime;

    /**
     * Unix timestamp
     */
    private Integer startTime;

    /**
     * The kick-off time of the first half or the second half, unix timestamp
     */
    private Integer halfStartTime;

    /**
     * 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
     */
    private Integer status;

    private String homeId;

    private String homeName;

    private String awayId;

    private String awayName;

    private Integer homeScore;

    private Integer awayScore;

    /**
     * First half score, home team
     */
    private Integer homeHalfScore;

    /**
     * First half score, away team
     */
    private Integer awayHalfScore;

    private Integer homeRed;

    private Integer awayRed;

    private Integer homeYellow;

    private Integer awayYellow;

    private Integer homeCorner;

    private Integer awayCorner;

    /**
     * The ranking of the team in the league, home team
     */
    private String homeRank;

    /**
     * The ranking of the team in the league, away team
     */
    private String awayRank;

    /**
     * e.g. 2019-2020
     */
    private String season;

    /**
     * Matches with [Interface Cup Stage Profile] stageId; To distinguish the different stages of the cup, the league does not return this data.
     */
    private String stageId;

    /**
     * League round or cup stage, e.g. 10
     */
    private String round;

    /**
     * Cup group, e.g. A
     */
    private String leagueGroup;

    /**
     * e.g. Camp Nou
     */
    private String location;

    /**
     * e.g. Clear
     */
    private String weather;

    /**
     * e.g. 14℃～15℃
     */
    private String temperature;

    /**
     * Special case description of the match, e.g. Match end up with [0-3], due to (Torpedo-MAZ Minsk) withdraw from the match
     */
    private String matchExplain;

    /**
     * Is there Lineup data?
     */
    private Boolean hasLineup;

    /**
     * Is it a neutral venue?
     */
    private Boolean neutral;

    /**
     * 1: Home kickoff 2: Away kickoff
     */
    private Integer exKickOff;

    /**
     * How many minutes does the match have in regular time?
     */
    private Integer exMinute;

    /**
     * Regular time score, home team
     */
    private Integer exHomeScore;

    /**
     * Regular time score, away team
     */
    private Integer exAwayScore;

    /**
     * 1: Normal matches extratime ends; 2: Special matches (e.g. beach football, indoor football) extratime ends; 3: The match in extra time
     */
    private Integer exExtraTimeStatus;

    /**
     * Extra time score, home team
     */
    private Integer exExtraHomeScore;

    /**
     * Extra time score, away team
     */
    private Integer exExtraAwayScore;

    /**
     * Penalty score, home team
     */
    private Integer exPenHomeScore;

    /**
     * Penalty score, away team
     */
    private Integer exPenAwayScore;

    private Integer exTwoRoundsHomeScore;

    private Integer exTwoRoundsAwayScore;

    /**
     * Winner of the match 1: Home 2: Away
     */
    private Integer exWinner;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
