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
    @ApiModelProperty("比赛ID")
    private String matchId;

    @ApiModelProperty("companyId")
    private String companyId;

    /**
     * 1: prematch; 2: inplay
     */
    @ApiModelProperty("1: 今日; 2: 滚盘")
    private Integer status;

    /**
     * CORRECT_SCORE[全场]，CORRECT_SCORE_HALL[半场]
     */
    @ApiModelProperty("类型：CORRECT_SCORE[全场]，CORRECT_SCORE_HALL[半场]")
    private String type;

    @ApiModelProperty("投注类型，JSON")
    private String bettingOddsItems;

    @ApiModelProperty("其他比分赔率")
    private String otherScoresOdds;

    @ApiModelProperty("修改时间")
    private Integer changeTime;
}
