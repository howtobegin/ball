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
@ApiModel("比赛波胆指数信息")
public class MatchOddsScoreResp {

    @ApiModelProperty("比赛ID")
    private MatchResp match;

    @ApiModelProperty("指数信息key(CORRECT_SCORE[全场]，CORRECT_SCORE_HALL[半场]), value(指数信息)")
    private Map<HandicapType, OddsScoreResp> odds;
}
