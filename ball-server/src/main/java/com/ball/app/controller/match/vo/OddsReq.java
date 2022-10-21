package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/10/20 上午10:38
 */
@Getter
@Setter
public class OddsReq {
    @ApiModelProperty("1 滚球；2 今日；3 早盘；如果是滚球，后面2个参数不传，今日和早盘，必须传联赛")
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "联赛ID", required = true)
    private String leagueId;

    @ApiModelProperty(value = "比赛ID，需要查某个比赛时传", required = true)
    private String matchId;
}
