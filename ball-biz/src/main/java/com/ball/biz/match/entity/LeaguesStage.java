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
@TableName("ds_leagues_stage")
public class LeaguesStage extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String leagueId;

    /**
     * e.g. 2018
     */
    private String season;

    private String stageId;

    /**
     * e.g. Qualifying Round
     */
    private String stageName;

    /**
     * Is it a group?
     */
    private Boolean group;

    /**
     * The number of groups.
     */
    private String groupNum;

    /**
     * Is it currently in progress?
     */
    private Boolean currStage;

    /**
     * The order of the different stages in the same cup.
     */
    private String stageOrder;

    /**
     * The number of qualifying teams.
     */
    private String lineCount;

    /**
     * Whether this stage of the league is a two-round match?（One home and one away, two matches are decided to win or lose）
     */
    private Boolean hasTwoLegs;

    /**
     * The number of qualifying teams in each group. This item may not be available, if there is, it will take precedence over lineCount.
     */
    private String groupLineupCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
