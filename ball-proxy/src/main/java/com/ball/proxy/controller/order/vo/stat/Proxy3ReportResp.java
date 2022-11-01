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
@ApiModel("注单报表-登3返回信息")
public class Proxy3ReportResp {
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

    @ApiModelProperty("会员 - 会员输赢分值")
    private BigDecimal userResultAmount;

    @ApiModelProperty("代理商币值")
    private BigDecimal userCurrencyAmount;
}
