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
 * @since 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_odds_score")
public class OddsScore extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long bizNo;

    private String matchId;

    private String companyId;

    /**
     * 1: prematch; 2: inplay
     */
    private Boolean status;

    /**
     * 1: full; 2: half
     */
    private Integer type;

    private Integer homeScore;

    private Integer awayScore;

    private String odds;

    private String otherOdds;

    private Integer changeTime;

    /**
     * Is this bet closed?
     */
    private Boolean isClose;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
