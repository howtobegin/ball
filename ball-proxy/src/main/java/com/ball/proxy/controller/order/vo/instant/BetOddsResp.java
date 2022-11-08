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

    @ApiModelProperty("HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场], CORRECT_SCORE[波胆],CORRECT_SCORE_HALL[半场波胆]")
    private String handicapType;

    @ApiModelProperty("投注选项，选队伍的传：主队 HOME、客队 AWAY、平 DRAW；大小就传：OVER 大、UNDER 小")
    private String betOption;

    @ApiModelProperty("下注金额")
    private BigDecimal amount;

    /**
     * 翻译后的即使赔率
     */
    @ApiModelProperty("翻译后的即使赔率")
    private String instantHandicap;

    /**
     * 投注结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平
     */
    @ApiModelProperty("投注结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平")
    private String betResult;

}
