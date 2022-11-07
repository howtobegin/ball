package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@Builder
@ApiModel("主页 - 占成收入返回信息")
public class IncomeReportResp {
    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("占成收入")
    private BigDecimal amount;
}
