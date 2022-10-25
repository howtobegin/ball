package com.ball.app.controller.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/25 上午10:59
 */
@Getter
@Setter
@ApiModel("投注记录请求信息")
public class BetHistoryReq {
    @ApiModelProperty("1 交易状况；2 账户历史")
    private Integer type;
}
