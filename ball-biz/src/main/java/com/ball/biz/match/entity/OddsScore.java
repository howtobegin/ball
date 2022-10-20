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
@TableName("ds_odds_score")
public class OddsScore extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String matchId;

    private String companyId;

    /**
     * 1: prematch; 2: inplay
     */
    private Boolean status;

    /**
     * CORRECT_SCORE[全场]，CORRECT_SCORE_HALL[半场]
     */
    private String type;

    private String bettingOddsItems;

    private String otherScoresOdds;

    private Integer changeTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
