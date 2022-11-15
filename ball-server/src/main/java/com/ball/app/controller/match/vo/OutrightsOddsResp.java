package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/11/15 下午5:45
 */
@Getter
@Setter
@ApiModel("世界杯玩法赔率返回信息")
public class OutrightsOddsResp {
    @ApiModelProperty("bizNo")
    private String bizNo;

    @ApiModelProperty("odds")
    private String odds;

    @Builder
    public OutrightsOddsResp(String bizNo, String odds) {
        this.bizNo = bizNo;
        this.odds = odds;
    }
}
