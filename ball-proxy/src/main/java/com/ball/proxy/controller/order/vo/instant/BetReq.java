package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 投注请求信息")
public class BetReq {
    @ApiModelProperty("运动类型：1 足球")
    @NotNull
    private Integer sport;

    @ApiModelProperty("比赛ID")
    @NotBlank
    private String matchId;
}
