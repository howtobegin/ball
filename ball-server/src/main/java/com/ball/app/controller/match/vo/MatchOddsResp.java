package com.ball.app.controller.match.vo;

import com.ball.biz.bet.enums.HandicapType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/20 下午3:24
 */
@Getter
@Setter
@ApiModel("比赛指数信息")
public class MatchOddsResp {

    @ApiModelProperty("比赛ID")
    private MatchResp match;

    @ApiModelProperty("全场指数信息key:HANDICAP[让球全场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场] ")
    private Map<HandicapType, OddsResp> odds;

    @ApiModelProperty("半场指数信息key:HANDICAP_HALF[让球半场],OVER_UNDER_HALF[大小球半场] ")
    private Map<HandicapType, OddsResp> halfOdds;
}
