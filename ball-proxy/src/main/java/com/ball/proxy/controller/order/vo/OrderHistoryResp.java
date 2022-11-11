package com.ball.proxy.controller.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/10/27 下午3:29
 */
@Getter
@Setter
@ApiModel("账户历史返回信息")
public class OrderHistoryResp {
    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("周")
    private Integer week;

    @ApiModelProperty("投注金额")
    private BigDecimal betAmount;

    @ApiModelProperty("有效金额")
    private BigDecimal validAmount;

    @ApiModelProperty("派彩结果")
    private BigDecimal winAmount;
}
