package com.ball.app.controller.match.vo;

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
public class OddsReq {
    @ApiModelProperty(value = "比赛ID", required = true)
    @NotBlank(message = "oldPassword must be not null")
    private String matchId;
}
