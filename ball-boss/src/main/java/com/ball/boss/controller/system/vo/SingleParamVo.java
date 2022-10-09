package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "单参数请求vo")
public class SingleParamVo<T> {
    @NotNull(message = "请求参数不可为空")
    @Valid
    @ApiModelProperty(value = "请求参数", required = true)
    private T data;
}
