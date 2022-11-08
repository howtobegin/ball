package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/11/1 下午6:20
 */
@Getter
@Setter
@ApiModel("投注信息")
public class BetResp {

    @ApiModelProperty("联赛信息")
    private LeagueResp league;

    @ApiModelProperty("比赛信息")
    private MatchResp match;


    @ApiModelProperty("投注信息")
    private List<BetOddsResp> list;

}
