package com.ball.proxy.controller.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/11/11 下午3:43
 */
@Getter
@Setter
@ApiModel("订单请求信息")
public class OrderReq {
    @ApiModelProperty("用户ID")
    @NotNull
    private Long userId;
}
