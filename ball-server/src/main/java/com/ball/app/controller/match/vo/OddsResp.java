package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/20 上午10:39
 */
@Getter
@Setter
@ApiModel("指数信息")
public class OddsResp {
    @ApiModelProperty("bizNo")
    private String bizNo;

    @ApiModelProperty("比赛ID")
    private String matchId;

    /**
     * type(HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场])
     */
    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("即时盘口")
    private String instantHandicap;

    @ApiModelProperty("即时盘口,解析之后的，前端显示用这个")
    private String instantHandicapDesc;

    @ApiModelProperty("主队即时赔率")
    private String instantHome;

    @ApiModelProperty("客队即时赔率")
    private String instantAway;

    private String instantDraw;

    @ApiModelProperty("即时盘大球赔率")
    private String instantOver;

    @ApiModelProperty("即时盘小球赔率")
    private String instantUnder;

    /**
     * The bet may be closed temporarily when the system is being maintained.
     */
    @ApiModelProperty("是否维护")
    private Boolean maintenance;

    /**
     * Is there inplay odds?
     */
    @ApiModelProperty("是否滚盘")
    private Boolean inPlay;

    /**
     * When the handicapIndex is 1, it is the data of the main market.
     */
    @ApiModelProperty("盘口索引")
    private Integer handicapIndex;

    /**
     * handicap count
     */
    @ApiModelProperty("盘口数量")
    private Integer handicapCount;

    /**
     * Change time, unix timestamp
     */
    @ApiModelProperty("盘口变化时间")
    private Integer changeTime;

    /**
     * Is this bet closed?
     */
    @ApiModelProperty("是否关闭")
    private Boolean isClose;

    /**
     * 0:Unable to judge 1:Early Odds 2:Instant odds(after the early odds before the match) 3:Inplay odds
     */
    @ApiModelProperty("赔率类型")
    private Integer oddsType;
}
