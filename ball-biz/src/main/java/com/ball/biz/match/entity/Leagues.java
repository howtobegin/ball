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
@TableName("ds_leagues")
public class Leagues extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String leagueId;

    /**
     * 1: League 2: Cup
     */
    private Boolean type;

    /**
     * RGB color code string, e.g. #9933FF
     */
    private String color;

    /**
     * League logo url. The picture is saved for local use, please do not call it directly.
     */
    private String logo;

    /**
     * Full name, e.g. Brazil Serie A
     */
    private String name;

    /**
     * Short name, e.g. BRA D1
     */
    private String shortName;

    /**
     * The on-going sub league of the league, e.g. Western Paly Off
     */
    private String subLeagueName;

    private Integer totalRound;

    private Integer currentRound;

    /**
     * e.g. 2018-2019
     */
    private String currentSeason;

    private String countryId;

    /**
     * Country or region name, e.g. Brazil countryLogostringCountry logo url.The picture is saved for local use, please do not call it directly.
     */
    private String country;

    /**
     * 0:International 1:Europe 2: America 3: Asia 4: Oceania 5: Africa
     */
    private Integer areaId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
