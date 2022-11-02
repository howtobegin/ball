package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/11/1 下午6:20
 */
@Getter
@Setter
@ApiModel("联盟返回信息")
public class LeagueResp {
    @ApiModelProperty("联赛名称")
    private String leagueName;

    @ApiModelProperty("联赛中文名称")
    private String leagueNameZh;

    @ApiModelProperty("投注笔数")
    private Long betCount;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;
}
