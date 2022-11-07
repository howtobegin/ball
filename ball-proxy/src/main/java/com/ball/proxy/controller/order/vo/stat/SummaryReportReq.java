package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@ApiModel("注单报表请求信息")
public class SummaryReportReq {
    @ApiModelProperty("日期类型：1 昨天；2 上周；3 本期")
    @NotNull
    private Integer dateType;

    @ApiModelProperty("代理账号ID，如果为空，则为当前登录账号数据")
    private Long proxyUserId;
}
