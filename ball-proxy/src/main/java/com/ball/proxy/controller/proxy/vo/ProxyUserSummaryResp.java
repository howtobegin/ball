package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author JimChery
 */
@Setter
@Getter
public class ProxyUserSummaryResp {
    @ApiModelProperty("用户编号")
    private Long userNo;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("额度模式 RECOVERY:自动恢复 BALANCE:余额浮动")
    private String balanceMode;
}
