package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "审核日志请求信息")
public class CheckLogRequestVo {
    @ApiModelProperty("业务编号")
    @NotBlank(message = "业务编号不可为空")
    private String bizId;

    @ApiModelProperty("业务代码 WITHDRAW_COIN 提币  USER_CERT_AUTH  实名认证")
    @NotBlank(message = "业务代码不可为空")
    private String bizCode;
}
