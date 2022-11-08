package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lhl
 * @date 2022/11/1 下午6:20
 */
@Getter
@Setter
@ApiModel("比赛返回信息")
public class MatchResp {

    @ApiModelProperty("比赛id")
    private String matchId;

    @ApiModelProperty("主队名称")
    private String homeName;

    @ApiModelProperty("主队中文名称")
    private String homeNameZh;

    @ApiModelProperty("客队名称")
    private String awayName;

    @ApiModelProperty("客队中文名称")
    private String awayNameZh;

    @ApiModelProperty("主队得分")
    private Integer homeScore;

    @ApiModelProperty("主队半场得分")
    private Integer homeHalfScore;

    @ApiModelProperty("客队得分")
    private Integer awayScore;

    @ApiModelProperty("客队半场得分")
    private Integer awayHalfScore;

    @ApiModelProperty("投注笔数")
    private Long betCount;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty("比赛时间")
    private LocalDateTime matchDate;
}
