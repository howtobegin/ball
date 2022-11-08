package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 早盘明细请求信息")
public class EarlyReq {
    @ApiModelProperty("运动类型：1 足球")
    @NotNull
    private Integer sport;

    @ApiModelProperty("日期类型：1 具体日期；2 未来（此时date传页面最后一个日期）")
    @NotNull
    private Integer dateType;

    @ApiModelProperty("日期，dataType=4时，必传")
    @NotNull
    private LocalDate date;

    @ApiModelProperty("联赛ID，不传表示查所有")
    private String leagueId;

    @ApiModelProperty("下注金额大于值")
    private String biggerBetAmount;

    @ApiModelProperty("下线，不传表示查所有")
    private List<Long> userIds;

    @ApiModelProperty("观看类型：1 所有；2 我的占成")
    @NotNull
    private Integer dataType;
}
