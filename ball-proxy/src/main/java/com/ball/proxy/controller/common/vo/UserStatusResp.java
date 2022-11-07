package com.ball.proxy.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author JimChery
 * @since 2022-11-07 17:30
 */
@Setter
@Getter
@Accessors(chain = true)
public class UserStatusResp {
    @ApiModelProperty("启用人数")
    private Integer normalCount = 0;

    @ApiModelProperty("禁用人数")
    private Integer lockCount = 0;
}
