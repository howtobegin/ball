package com.ball.app.controller.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/10/25 上午10:59
 */
@Getter
@Setter
@ApiModel("投注记录请求信息")
public class BetHistoryReq {
    @ApiModelProperty("开始时间")
    @NotNull
    private LocalDate start;

    @ApiModelProperty("结束时间")
    @NotNull
    private LocalDate end;
}
