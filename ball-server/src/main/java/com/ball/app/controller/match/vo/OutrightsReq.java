package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/11/15 下午5:45
 */
@Getter
@Setter
public class OutrightsReq {
    @ApiModelProperty("1 赛事；2 小组；3 冠军")
    @NotNull
    private Integer type;
}
