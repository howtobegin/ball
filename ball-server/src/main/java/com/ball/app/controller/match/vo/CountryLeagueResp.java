package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/10/25 下午5:31
 */
@Getter
@Setter
@ApiModel("国家联赛信息")
public class CountryLeagueResp {

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("联赛数据")
    private List<LeagueResp> leagueResp;

    @Builder
    public CountryLeagueResp(String country, List<LeagueResp> leagueResp) {
        this.country = country;
        this.leagueResp = leagueResp;
    }
}
