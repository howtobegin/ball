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
 * @since 2022-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_odds_outrights")
public class OddsOutrights extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long bizNo;

    /**
     * 联赛ID
     */
    private String leagueId;

    /**
     * 联赛名称
     */
    private String leagueName;

    /**
     * 联赛名称(中文)
     */
    private String leagueNameZh;

    /**
     * e.g. BRA D1
     */
    private String leagueShortName;

    /**
     * e.g. 2019-2020
     */
    private String season;

    /**
     * 投注类型
     */
    private Integer typeId;

    /**
     * 投注类型名称
     */
    private String type;

    /**
     * typeId=5时等于playerId，其他情况等于teamId
     */
    private String itemId;

    /**
     * 球队名称或球员名称
     */
    private String item;

    /**
     * 球队名称(中文)
     */
    private String itemZh;

    /**
     * 赔率公司ID
     */
    private String companyId;

    /**
     * 赔率公司名称
     */
    private String company;

    /**
     * 赔率
     */
    private String odds;

    /**
     * 是否可以投注(1: 是；2：否)
     */
    private Integer valid;

    /**
     * Create time
     */
    private LocalDateTime createTime;

    /**
     * Last update time
     */
    private LocalDateTime updateTime;


}
