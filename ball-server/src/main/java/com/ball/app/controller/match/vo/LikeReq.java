package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/10/20 上午10:38
 */
@Getter
@Setter
@ApiModel(description = "收藏请求信息")
public class LikeReq {
    @ApiModelProperty(value = "1 滚球；2 今日；3 早盘；", required = true)
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "联赛ID", required = true)
    @NotBlank
    private String leagueId;

    @ApiModelProperty(value = "比赛ID", required = true)
    @NotBlank
    private String matchId;
}
