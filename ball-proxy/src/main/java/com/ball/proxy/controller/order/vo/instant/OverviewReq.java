package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 总览请求信息")
public class OverviewReq {
    @ApiModelProperty("观看类型：1 所有；2 我的占成")
    private Integer type;
}
