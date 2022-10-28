package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author lhl
 * @date 2022/10/20 上午10:38
 */
@Getter
@Setter
@ApiModel(description = "取消收藏请求信息")
public class UnlikeReq {
    @ApiModelProperty(value = "比赛ID", required = true)
    @NotBlank
    private String matchId;
}
