package com.ball.app.controller.match.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fanyongpeng
 * @date 10/25/22
 **/
@Data
@ApiModel("赛程列表请求")
public class ScheduleReq {
    @ApiModelProperty("日期")
    private LocalDateTime date;

}
