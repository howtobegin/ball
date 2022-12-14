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
@ApiModel("账户信息")
public class AccountResp {
    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("授信额度")
    private BigDecimal allowance;

    @ApiModelProperty("额度模式  RECOVERY 自动恢复 BALANCE余额浮动")
    private String allowanceMode;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("余额")
    private BigDecimal balance;

    @ApiModelProperty("冻结金额")
    private BigDecimal freezeAmount;

    @ApiModelProperty("可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
