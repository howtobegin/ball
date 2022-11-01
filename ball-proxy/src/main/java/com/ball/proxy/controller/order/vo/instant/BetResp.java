package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/11/1 下午6:20
 */
@Getter
@Setter
@ApiModel("投注信息")
public class BetResp {

    @ApiModelProperty("主要玩法投注信息")
    private List<BetOddsResp> oddsRespList;

    @ApiModelProperty("波胆投注信息")
    private List<BetOddsResp> oddsScoreRespList;

}
