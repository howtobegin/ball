package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-10-28 15:39
 */
@Setter
@Getter
public class ProxyRefundConfigReq {
    @ApiModelProperty(value = "A类用户设定,让球和滚球让球必传")
    private BigDecimal a;

    @ApiModelProperty(value = "B类用户设定,让球和滚球让球必传")
    private BigDecimal b;

    @ApiModelProperty(value = "C类用户设定,让球和滚球让球必传")
    private BigDecimal c;

    @ApiModelProperty(value = "D类用户设定,让球和滚球让球必传")
    private BigDecimal d;

    @ApiModelProperty(value = "单场最高", required = true)
    @NotNull
    private BigDecimal matchLimit;

    @ApiModelProperty(value = "单注最高", required = true)
    @NotNull
    private BigDecimal orderLimit;

    @ApiModelProperty(value = "最低投注限额")
    private BigDecimal min;

    @ApiModelProperty(value = "玩法 HOE:让球, HOE_INPAY:滚球让球 WINNER_AND_WINNER_INPAY:独赢, OTHER:其他玩法, OTHER_INPAY:滚球其他玩法", required = true)
    @NotBlank
    private String type;

    @ApiModelProperty(value = "大类型 FOOTBALL:足球", required = true)
    @NotBlank(message = "sport must be not null")
    private String sport;
}
