package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@Builder
@ApiModel("主页 - 四合一接口返回信息")
public class FourOneReportResp {
    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("占成收入/数量/投注人数")
    private String amount;
}
