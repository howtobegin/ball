package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/
@Data
@ApiModel(description = "用户退水限额配置")
public class UserTradeConfigReq {
    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("用户id")
    @NotNull
    private Long userNo;

    /**
     * 类型
     */
    @ApiModelProperty("类型 HOE 让球, 大 / 小, 单 / 双; HOE_INPAY 滚球让球, 滚球大 / 小, 滚球单 / 双;" +
            " WINNER_AND_WINNER_INPAY 独赢, 滚球独赢; OTHER 其他玩法; OTHER_INPAY 滚球其他")
    @NotBlank
    private String type;

    /**
     * d类用户退水比例
     */
    @ApiModelProperty("用户退水比例")
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal v;

    /**
     * 单场最高限额
     */
    @ApiModelProperty("单场最高限额")
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal matchLimit;

    /**
     * 单注最高限额
     */
    @ApiModelProperty("单注最高限额")
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal orderLimit;

    /**
     * 单注最低
     */
    @ApiModelProperty("单注最低")
    private BigDecimal min;

    /**
     * 用户级别 A B C D
     */
    @ApiModelProperty("用户退水级别 A B C D")
    @NotBlank
    private String userLevel;
}
