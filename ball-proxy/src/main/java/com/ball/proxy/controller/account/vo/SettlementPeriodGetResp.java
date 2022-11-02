package com.ball.proxy.controller.account.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fanyongpeng
 * @date 11/1/22
 **/
@Data
@ApiModel(description = "账期信息")
public class SettlementPeriodGetResp {

    @ApiModelProperty("账期阶段")
    private String year;

    @ApiModelProperty("账期列表")
    private List<SettlementPeriodResp> list;
}
