package com.ball.proxy.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author JimChery
 * @since 2022-11-07 17:28
 */
@Setter
@Getter
public class UserTypeReq {
    @ApiModelProperty(value = "用户类型 1:会员 6:总代理 7:代理商", required = true)
    @NotNull
    private Integer userType;
}
