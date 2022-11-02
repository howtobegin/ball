package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 赛事结果概要
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
@Data
@ApiModel(description = "赛事结果概要")
public class OrderSummaryResp {

    /**
     * 投注日期
     */
    @ApiModelProperty("投注日期")
    private LocalDate summaryDate;

    /**
     * 运动类型：1 足球
     */
    @ApiModelProperty("运动类型：1 足球")
    private Integer sport;

    /**
     * 有结果
     */
    @ApiModelProperty("有结果")
    private Long completeCount;

    /**
     * 未有结果
     */
    @ApiModelProperty("未有结果")
    private Long undoneCount;
}
