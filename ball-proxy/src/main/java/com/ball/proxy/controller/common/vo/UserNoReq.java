package com.ball.proxy.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author JimChery
 * @since 2022-11-02 14:57
 */
@Setter
@Getter
public class UserNoReq {
    @ApiModelProperty(value = "会员或代理编号", required = true)
    @NotNull
    private Long userNo;
}
