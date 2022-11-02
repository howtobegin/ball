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
@ApiModel("投注赔率波胆信息")
public class BetOddsScoreResp {

    @ApiModelProperty("CORRECT_SCORE[波胆],CORRECT_SCORE_HALF[半场波胆]")
    private String handicapType;

    @ApiModelProperty("投注选项，SCORE 具体比分、SCORE_OTHER 其他比分")
    private String betOption;

    @ApiModelProperty("主队比分")
    private Integer homeScore;

    @ApiModelProperty("客队比分")
    private Integer awayScore;

    @ApiModelProperty("投注金额")
    private BigDecimal betAmount;
}
