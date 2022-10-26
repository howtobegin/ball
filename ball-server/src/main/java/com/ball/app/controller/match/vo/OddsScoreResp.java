package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/20 上午10:39
 */
@Getter
@Setter
public class OddsScoreResp {
    @ApiModelProperty("bizNo")
    private Long bizNo;

    @ApiModelProperty("比赛ID")
    private String matchId;

    @ApiModelProperty("companyId")
    private String companyId;

    /**
     * 1: prematch; 2: inplay
     */
    @ApiModelProperty("1: 今日和早盘; 2: 滚盘")
    private Integer status;

    @ApiModelProperty("类型：1: full; 2: half")
    private Integer type;

    @ApiModelProperty("主队分")
    private Integer homeScore;

    @ApiModelProperty("客队分")
    private Integer awayScore;

    @ApiModelProperty("赔率")
    private String odds;

    @ApiModelProperty("其他赔率")
    private String otherOdds;

    @ApiModelProperty("赔率修改睡觉")
    private Integer changeTime;

    /**
     * Is this bet closed?
     */
    @ApiModelProperty("是否关闭")
    private Boolean isClose;
}
