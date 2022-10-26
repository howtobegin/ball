package com.ball.app.controller.order.vo;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/20 下午3:47
 */
@Getter
@Setter
@ApiModel("下注请求信息")
public class BetReq {
    @ApiModelProperty("盘口类型：HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场]，CORRECT_SCORE[全场]，CORRECT_SCORE_HALL[半场]")
    @NotNull
    private HandicapType handicapType;

    @ApiModelProperty("bizNo")
    @NotBlank
    private String bizNo;

    @ApiModelProperty("投注选项，选队伍的传：主队 HOME、客队 AWAY、平 DRAW；大小就传：OVER 大、UNDER 小；波胆投注：SCORE 具体比分、SCORE_OTHER 其他比分")
    @NotBlank
    private BetOption betOption;

    @ApiModelProperty("投注金额")
    @NotNull
    @Min(0)
    private BigDecimal betAmount;
}
