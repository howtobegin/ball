package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/11/15 下午5:45
 */
@Getter
@Setter
@ApiModel("世界杯冠军返回信息")
public class OutrightsWinnerResp {
    @ApiModelProperty("bizNo")
    private String bizNo;

    @ApiModelProperty("联赛名称(中文)")
    private String leagueNameZh;

    @ApiModelProperty("球队/国家/球员 ID")
    private String itemId;

    @ApiModelProperty("球队/国家/球员中文名")
    private String itemZh;

    @ApiModelProperty("赔率")
    private String odds;

    @Builder
    public OutrightsWinnerResp(String bizNo, String leagueNameZh, String itemId, String itemZh, String odds) {
        this.bizNo = bizNo;
        this.leagueNameZh = leagueNameZh;
        this.itemId = itemId;
        this.itemZh = itemZh;
        this.odds = odds;
    }
}
