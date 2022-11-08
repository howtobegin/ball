package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@ApiModel("注单报表请求信息")
public class BaseReportReq {
    @ApiModelProperty("是否有结果")
    @NotNull
    private boolean hasResult;

    @ApiModelProperty("开始日期")
    @NotNull
    private LocalDate start;

    @ApiModelProperty("结束日期")
    @NotNull
    private LocalDate end;

    @ApiModelProperty("运动：1 足球")
    @NotNull
    private Integer sport;

    @ApiModelProperty("登1（点击账号进入下一级报表时，必传）")
    private Long proxy1Id;

    @ApiModelProperty("登2（点击账号进入下一级报表时，必传）")
    private Long proxy2Id;

    @ApiModelProperty("登3（点击账号进入下一级报表时，必传）")
    private Long proxy3Id;

    @ApiModelProperty("用户（点击账号进入下一级报表时，必传）")
    private Long userId;
}
