package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 *
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("注单报表-登3返回信息")
public class Proxy3ReportResp {
    @ApiModelProperty("登3ID")
    private Long proxyId;

    @ApiModelProperty("登3账号")
    private String proxyAccount;

    @ApiModelProperty("登3名称")
    private String proxyName;

    @ApiModelProperty("笔数")
    private Long betCount;

    @ApiModelProperty("下注金额RMB")
    private BigDecimal betAmount;

    @ApiModelProperty("有效金额RMB")
    private BigDecimal validAmount;

    @ApiModelProperty("会员 - 会员输赢分值+退水 RMB")
    private BigDecimal userWinAmount;

    @ApiModelProperty("代理商币值 sum(原始币种的有效金额)")
    private BigDecimal proxyCurrencyAmount;

    @ApiModelProperty("代理商 下线输赢和")
    private BigDecimal proxyResultAmount;

    @ApiModelProperty("代理商占成 sum(proxy3Amount)")
    private BigDecimal proxyAmount;

    @ApiModelProperty("代理商结果 下线输赢和")
    private BigDecimal proxyResultAmount2;

    @ApiModelProperty("代理商实货量")
    private BigDecimal proxyValidAmount;

    @ApiModelProperty("总代理占成")
    private BigDecimal proxy3Rate;

    @ApiModelProperty("总代理结果")
    private BigDecimal proxyResultAmount3;

    @Builder
    public Proxy3ReportResp(Long proxyId, String proxyAccount, String proxyName, Long betCount, BigDecimal betAmount, BigDecimal validAmount, BigDecimal userWinAmount, BigDecimal proxyCurrencyAmount, BigDecimal proxyResultAmount, BigDecimal proxyAmount, BigDecimal proxyResultAmount2, BigDecimal proxyValidAmount, BigDecimal proxy3Rate, BigDecimal proxyResultAmount3) {
        this.proxyId = proxyId;
        this.proxyAccount = proxyAccount;
        this.proxyName = proxyName;
        this.betCount = betCount;
        this.betAmount = betAmount;
        this.validAmount = validAmount;
        this.userWinAmount = userWinAmount;
        this.proxyCurrencyAmount = proxyCurrencyAmount;
        this.proxyResultAmount = proxyResultAmount;
        this.proxyAmount = proxyAmount;
        this.proxyResultAmount2 = proxyResultAmount2;
        this.proxyValidAmount = proxyValidAmount;
        this.proxy3Rate = proxy3Rate;
        this.proxyResultAmount3 = proxyResultAmount3;
    }
}
