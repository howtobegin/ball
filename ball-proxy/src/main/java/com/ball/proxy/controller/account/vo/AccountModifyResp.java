package com.ball.proxy.controller.account.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ab
 */
@Setter
@Getter
@ApiModel("额度修改记录")
public class AccountModifyResp {
    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("存入")
    private BigDecimal deposit;

    @ApiModelProperty("提出")
    private BigDecimal withdraw;

    @ApiModelProperty("额度")
    private BigDecimal oldBalance;

    @ApiModelProperty("新额度")
    private BigDecimal newBalance;

    @ApiModelProperty("时间")
    private LocalDateTime createTime;

}
