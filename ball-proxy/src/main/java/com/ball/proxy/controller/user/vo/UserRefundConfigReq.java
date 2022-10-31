package com.ball.proxy.controller.user.vo;

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
public class UserRefundConfigReq {
    @ApiModelProperty(value = "退水设定")
    private BigDecimal v;

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
