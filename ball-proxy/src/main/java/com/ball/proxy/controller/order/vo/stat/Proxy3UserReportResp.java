package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@Builder
@ApiModel("注单报表-登3返回信息")
public class Proxy3UserReportResp {
    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("会员账号")
    private String userAccount;

    @ApiModelProperty("会员名称")
    private String userName;

    @ApiModelProperty("笔数")
    private Long betCount;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty("有效金额")
    private BigDecimal validAmount;

    @ApiModelProperty("会员 - 会员输赢分值+退水 RMB")
    private BigDecimal userWinAmount;

    @ApiModelProperty("会员币值")
    private BigDecimal userCurrencyAmount;

    @ApiModelProperty("代理商占成")
    private BigDecimal proxy3Percent;

    @ApiModelProperty("代理商结果A")
    private BigDecimal proxy3Amount;
}
