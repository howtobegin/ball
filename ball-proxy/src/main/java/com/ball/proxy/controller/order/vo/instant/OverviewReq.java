package com.ball.proxy.controller.order.vo.instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author lhl
 * @date 2022/11/1 下午5:53
 */@Getter
@Setter
@ApiModel("即时注单 - 总览请求信息")
public class OverviewReq {
    @ApiModelProperty("观看类型：1 所有；2 我的占成, 3 代理商占成，4 总代理+ 代理商占成")
    @NotNull
    private Integer dataType;

    @ApiModelProperty("开始时间")
//    @NotNull
    private LocalDateTime time;
}
