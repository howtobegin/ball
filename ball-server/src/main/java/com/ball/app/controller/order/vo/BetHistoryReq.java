package com.ball.app.controller.order.vo;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/10/25 上午10:59
 */
@Getter
@Setter
@ApiModel("投注记录请求信息")
public class BetHistoryReq extends Paging {
    @ApiModelProperty("1 交易状况；2 账户历史")
    private Integer type;

    @ApiModelProperty("开始时间")
    private LocalDate start;

    @ApiModelProperty("结束时间")
    private LocalDate end;
}
