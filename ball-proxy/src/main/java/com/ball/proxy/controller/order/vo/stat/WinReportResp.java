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
@ApiModel("主页 - 输/赢返回信息")
public class WinReportResp {
    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("输/赢")
    private BigDecimal amount;
}
