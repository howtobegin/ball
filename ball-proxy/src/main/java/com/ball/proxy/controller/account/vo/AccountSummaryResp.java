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
@ApiModel("账户概况")
public class AccountSummaryResp {
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


    @ApiModelProperty("当前账期")
    private String currentPeriod;

    @ApiModelProperty("本期完成天数")
    private Long periodFinishedDays;

    @ApiModelProperty("本期剩余天数")
    private Long periodLeftDays;

    @ApiModelProperty("本期会员数")
    private Integer periodUserCount;

    @ApiModelProperty("最后更改密码时间")
    private Long changePasswordTime;

    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;
}
