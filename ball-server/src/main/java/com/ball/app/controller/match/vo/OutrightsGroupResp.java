package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/11/15 下午5:45
 */
@Getter
@Setter
@ApiModel("世界杯小组返回信息")
public class OutrightsGroupResp {

    @ApiModelProperty("分组，比如A")
    private String group;

    @ApiModelProperty("赔率")
    private List<OutrightsGroupOddsResp> oddsList;

    @Builder
    public OutrightsGroupResp(String group, List<OutrightsGroupOddsResp> oddsList) {
        this.group = group;
        this.oddsList = oddsList;
    }
}
