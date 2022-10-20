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
@TableName("ds_odds")
public class Odds extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String matchId;

    /**
     * 1: Macauslot,3: Crown
     */
    private String companyId;

    /**
     * type(HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场])
     */
    private String type;

    private String initialHandicap;

    private String initialHome;

    private String initialAway;

    private String initialDraw;

    private String initialOver;

    private String initialUnder;

    private String instantHandicap;

    private String instantHome;

    private String instantAway;

    private String instantDraw;

    private String instantOver;

    private String instantUnder;

    /**
     * The bet may be closed temporarily when the system is being maintained.
     */
    private Boolean maintenance;

    /**
     * Is there inplay odds?
     */
    private Boolean inPlay;

    /**
     * When the handicapIndex is 1, it is the data of the main market.
     */
    private Integer handicapIndex;

    /**
     * handicap count
     */
    private Integer handicapCount;

    /**
     * Change time, unix timestamp
     */
    private Integer changeTime;

    /**
     * Is this bet closed?
     */
    private Boolean close;

    /**
     * 0:Unable to judge 1:Early Odds 2:Instant odds(after the early odds before the match) 3:Inplay odds
     */
    private Boolean oddsType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
