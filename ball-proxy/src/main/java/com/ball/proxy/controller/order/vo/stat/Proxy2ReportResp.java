package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 点查询返回
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("注单报表-登1/登2返回信息")
public class Proxy2ReportResp {
    @ApiModelProperty("登2ID")
    private Long proxyId;

    @ApiModelProperty("登1或登2账号")
    private String proxyAccount;

    @ApiModelProperty("登1或登2名称")
    private String proxyName;

    @ApiModelProperty("笔数")
    private Long betCount;

    @ApiModelProperty("下注金额RMB")
    private BigDecimal betAmount;

    @ApiModelProperty("有效金额RMB")
    private BigDecimal validAmount;

    @ApiModelProperty("会员 - 会员输赢分值+退水 RMB")
    private BigDecimal userWinAmount;

    @ApiModelProperty("代理商 下线输赢和")
    private BigDecimal proxyResultAmount;

    @ApiModelProperty("代理商结果 下线输赢和")
    private BigDecimal proxyResultAmount2;

    @ApiModelProperty("总代理结果")
    private BigDecimal proxyResultAmount3;

    @ApiModelProperty("总代理实货量")
    private BigDecimal proxyValidAmount;

    @ApiModelProperty("总代理百分比")
    private BigDecimal proxyRate;

    @Builder
    public Proxy2ReportResp(Long proxyId, String proxyAccount, String proxyName, Long betCount, BigDecimal betAmount, BigDecimal validAmount, BigDecimal userWinAmount, BigDecimal proxyResultAmount, BigDecimal proxyResultAmount2, BigDecimal proxyResultAmount3, BigDecimal proxyValidAmount, BigDecimal proxyRate) {
        this.proxyId = proxyId;
        this.proxyAccount = proxyAccount;
        this.proxyName = proxyName;
        this.betCount = betCount;
        this.betAmount = betAmount;
        this.validAmount = validAmount;
        this.userWinAmount = userWinAmount;
        this.proxyResultAmount = proxyResultAmount;
        this.proxyResultAmount2 = proxyResultAmount2;
        this.proxyResultAmount3 = proxyResultAmount3;
        this.proxyValidAmount = proxyValidAmount;
        this.proxyRate = proxyRate;
    }
}
