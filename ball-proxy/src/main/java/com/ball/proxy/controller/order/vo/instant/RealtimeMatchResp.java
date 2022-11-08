package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fanyongpeng
 * @date 11/8/22
 **/
@Data
@ApiModel("即时注单 返回信息")
public class RealtimeMatchResp {

    @ApiModelProperty("联赛列表")
    List<LeagueMatchResp> leagues;

    @ApiModelProperty("投注笔数")
    private Long betCount;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;
}
