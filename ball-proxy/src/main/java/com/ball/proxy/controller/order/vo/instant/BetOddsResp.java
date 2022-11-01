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
@ApiModel("投注赔率信息")
public class BetOddsResp {

    @ApiModelProperty("HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场]")
    private String handicapType;

    @ApiModelProperty("投注选项，选队伍的传：主队 HOME、客队 AWAY、平 DRAW；大小就传：OVER 大、UNDER 小")
    private String betOption;

    @ApiModelProperty("下注金额：主队或大")
    private BigDecimal betOneAmount;

    @ApiModelProperty("下注金额：客队或小")
    private BigDecimal betTwoAmount;

    @ApiModelProperty("下注金额：和局")
    private BigDecimal betDrawAmount;
}
