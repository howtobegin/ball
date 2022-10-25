package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lhl
 * @date 2022/10/20 上午10:38
 */
@Getter
@Setter
public class OddsScoreReq {
    @ApiModelProperty("1: 今日和早盘; 2: 滚盘")
    @NotNull
    private Integer status;

    @ApiModelProperty(value = "联赛ID集合", required = true)
    @NotEmpty
    private List<String> leagueIds;
}
