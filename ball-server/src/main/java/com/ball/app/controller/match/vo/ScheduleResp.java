package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fanyongpeng
 * @date 10/25/22
 **/
@Data
@ApiModel("单日赛程列表")
public class ScheduleResp {
    @ApiModelProperty("日期描述")
    String date;

    @ApiModelProperty("日期")
    LocalDateTime time;

    @ApiModelProperty("赛程列表")
    List<ScheduleDetailResp> list;

    public ScheduleResp(){};

    public ScheduleResp(String date, List<ScheduleDetailResp> list) {
        this.date = date;
        this.list = list;
    }
}
