package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 滚球/今日明细返回信息")
public class LeagueMatchResp {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("联赛信息")
    private LeagueResp league;

    @ApiModelProperty("比赛信息")
    private List<MatchResp> matchList;

    @ApiModelProperty("投注笔数")
    private Long betCount;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;

}
