package com.ball.boss.controller.system.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author JimChery
 */
@Getter
@Setter
public class GoogleCodeReq {
    @ApiModelProperty("谷歌验证码")
    @NotBlank(message = "code must be not null")
    private String code;
}
