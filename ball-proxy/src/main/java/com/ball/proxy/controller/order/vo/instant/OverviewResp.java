package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 总览返回信息")
public class OverviewResp {

    @ApiModelProperty("总投注笔数")
    private Long betCount;

    @ApiModelProperty("总投注数量")
    private BigDecimal betAmount;

    @ApiModelProperty("滚球：投注数量")
    private List<OverviewDetailResp> details;
}
