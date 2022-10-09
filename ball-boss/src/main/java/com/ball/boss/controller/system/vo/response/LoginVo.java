package com.ball.boss.controller.system.vo.response;

import com.ball.boss.service.system.model.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@ApiModel(description = "登录信息")
public class LoginVo {
    @ApiModelProperty("登录令牌")
    private String token;

    @ApiModelProperty("用户信息")
    private UserInfo userInfo;
}
