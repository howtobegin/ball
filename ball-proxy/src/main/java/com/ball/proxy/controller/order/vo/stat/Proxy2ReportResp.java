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
@ApiModel("注单报表-登2返回信息")
public class Proxy2ReportResp {
    @ApiModelProperty("代理商账号")
    private String proxyAccount;

    @ApiModelProperty("代理商名称")
    private String proxyName;

    @ApiModelProperty("笔数")
    private Long betCount;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty("有效金额")
    private BigDecimal validAmount;

    @ApiModelProperty("会员 - 会员输赢分值+退水")
    private BigDecimal userWinAmount;

    @ApiModelProperty("代理商币值")
    private BigDecimal proxyCurrencyAmount;

    @ApiModelProperty("代理商")
    private BigDecimal proxyResultAmount;

    @ApiModelProperty("代理商占成")
    private BigDecimal proxyAmount;

    @ApiModelProperty("代理商结果")
    private BigDecimal proxyResultAmount2;

    @ApiModelProperty("代理商实货量")
    private BigDecimal proxyValidAmount;

    @ApiModelProperty("总代理占成")
    private BigDecimal proxy1IncomeAmount;

    @ApiModelProperty("总代理结果")
    private BigDecimal proxyResultAmount3;
}
