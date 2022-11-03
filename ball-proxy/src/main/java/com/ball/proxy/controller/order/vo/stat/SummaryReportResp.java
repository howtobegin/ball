package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@ApiModel("主页 - 绩效概况返回信息")
public class SummaryReportResp {
    @ApiModelProperty("获利率")
    private BigDecimal profitRate;

    @ApiModelProperty("获利率相比昨天减少或增加百分比")
    private BigDecimal profitRateCompareYesterday;

    @ApiModelProperty("输/赢")
    private BigDecimal winAmount;

    @ApiModelProperty("输/赢相比昨天减少或增加百分比")
    private BigDecimal winAmountCompareYesterday;

    @ApiModelProperty("实货量")
    private BigDecimal validAmount;

    @ApiModelProperty("实货量相比昨天减少或增加百分比")
    private BigDecimal validAmountCompareYesterday;
}
