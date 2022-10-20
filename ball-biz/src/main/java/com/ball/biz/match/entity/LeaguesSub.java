package com.ball.biz.match.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.ball.base.model.Paging;

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
@TableName("ds_leagues_sub")
public class LeaguesSub extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * The league to which the sub-league belongs.
     */
    private String leagueId;

    private String subLeagueId;

    private String name;

    /**
     * The order of the different sub-libraries in the same league.
     */
    private Integer number;

    private Integer totalRound;

    private Integer currentRound;

    /**
     * e.g. 2018-2019
     */
    private String currentSeason;

    private String includeSeason;

    /**
     * Is there standing data？
     */
    private Boolean hasScore;

    /**
     * Whether this stage of the league is a two-round match?（One home and one away, two matches are decided to win or lose）
     */
    private Boolean hasTwoLegs;

    /**
     * Is this subleague in progress?
     */
    private Boolean currentSubLeague;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
