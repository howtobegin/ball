package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@Builder
@ApiModel("主页 - 投注人数返回信息")
public class BetCountReportResp {
    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("投注人数")
    private Long betCount;
}
