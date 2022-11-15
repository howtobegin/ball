package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/20 下午3:12
 */
@Getter
@Setter
@ApiModel(description = "联赛信息")
public class LeagueResp {

    @ApiModelProperty("联赛ID")
    private String leagueId;

    /**
     * 1: League 2: Cup
     */
    @ApiModelProperty("类型，1: League 2: Cup")
    private Integer type;

    @ApiModelProperty("0: no star 1: star")
    private Integer star;

    /**
     * RGB color code string, e.g. #9933FF
     */
    @ApiModelProperty("RGB color code string, e.g. #9933FF")
    private String color;

    /**
     * League logo url. The picture is saved for local use, please do not call it directly.
     */
    @ApiModelProperty("League logo url. The picture is saved for local use, please do not call it directly.")
    private String logo;

    /**
     * Full name, e.g. Brazil Serie A
     */
    @ApiModelProperty("全称Full name, e.g. Brazil Serie A")
    private String name;

    @ApiModelProperty("全称中文")
    private String nameZh;

    /**
     * Short name, e.g. BRA D1
     */
    @ApiModelProperty("简称Short name, e.g. BRA D1")
    private String shortName;

    /**
     * The on-going sub league of the league, e.g. Western Paly Off
     */
    @ApiModelProperty("The on-going sub league of the league, e.g. Western Paly Off")
    private String subLeagueName;

    @ApiModelProperty("countryId")
    private String countryId;

    /**
     * Country or region name, e.g. Brazil countryLogostringCountry logo url.The picture is saved for local use, please do not call it directly.
     */
    @ApiModelProperty("Country or region name, e.g. Brazil countryLogostringCountry logo url.The picture is saved for local use, please do not call it directly.")
    private String country;

    @ApiModelProperty("国家中文")
    private String countryZh;

    @ApiModelProperty("联赛分组，比如A小组")
    private String leagueGroup;
}
