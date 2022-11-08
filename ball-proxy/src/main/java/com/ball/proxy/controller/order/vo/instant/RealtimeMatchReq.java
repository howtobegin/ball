package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 请求信息")
public class RealtimeMatchReq {

    @ApiModelProperty("开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime time;

    @ApiModelProperty("运动类型：1 足球")
    @NotNull(message = "运动类型不能为空")
    private Integer sport;

    @ApiModelProperty(" 投注类型：1 早盘；2 赛前即时；3 滚盘")
    @NotNull(message = "投注类型不能为空")
    @Min(1)
    @Max(3)
    private Integer oddsType;

    @ApiModelProperty("联赛ID，不传表示查所有,逗号分割")
    private String leagueIds;

    @ApiModelProperty("下注金额大于值")
    private BigDecimal betAmount;

    @ApiModelProperty("下线，不传表示查所有,逗号分割")
    private String userIds;

    @ApiModelProperty("观看类型：1 所有；2 我的占成, 3 代理商占成，4 总代理+ 代理商占成")
    @NotNull(message = "观看类型不能为空")
    private Integer dataType;
}
