package com.ball.boss.controller.system.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "登录请求信息")
public class LoginRequestVo {
    @ApiModelProperty("账户编号")
    @NotBlank(message = "账户编号不可为空")
    private String accountId;

    @ApiModelProperty("账户密码")
    @NotBlank(message = "账户密码不可为空")
    private String password;

    @ApiModelProperty("谷歌验证码")
    private String googleCode;
}
