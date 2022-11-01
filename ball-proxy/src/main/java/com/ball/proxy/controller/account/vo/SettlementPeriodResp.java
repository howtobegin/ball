package com.ball.proxy.controller.account.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fanyongpeng
 * @date 11/1/22
 **/
@Data
@ApiModel(description = "账期详情")
public class SettlementPeriodResp {

    private Long id;

    @ApiModelProperty("账期阶段")
    private String period;

    @ApiModelProperty("开始日期")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty("结束日期")
    private LocalDateTime endDate;
}
