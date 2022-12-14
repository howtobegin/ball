package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/10/20 下午12:01
 */
@Getter
@Setter
public class LeagueReq {
    @ApiModelProperty("2 今日；3 早盘")
    @NotNull
    private Integer type;
}
