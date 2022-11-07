package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */
@Getter
@Setter
@ApiModel("即时注单 - 总览返回信息")
public class OverviewDetailResp {
    @ApiModelProperty("运动类型：0 总计, 1 足球")
    private Integer sport;

    @ApiModelProperty("滚球：投注笔数")
    private Long inplayBetCount;

    @ApiModelProperty("滚球：投注数量")
    private BigDecimal inplayBetAmount;

    @ApiModelProperty("今日：投注笔数")
    private Long todayBetCount;

    @ApiModelProperty("今日：投注数量")
    private BigDecimal todayBetAmount;

    @ApiModelProperty("早盘：投注笔数")
    private Long earlyBetCount;

    @ApiModelProperty("早盘：投注数量")
    private BigDecimal earlyBetAmount;
}
