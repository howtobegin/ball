package com.ball.app.controller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author littlehow
 */
@Setter
@Getter
@Accessors(chain = true)
public class LoginResp {
    @ApiModelProperty("用户编号")
    private Long userNo;

    @ApiModelProperty("登入账号")
    private String loginAccount;

    @ApiModelProperty("密码修改标志 1:已修改 0:未修改")
    private Integer changePasswordFlag;

    @ApiModelProperty("账号修改标志 1:已修改 0:未修改")
    private Integer changeAccountFlag;

    @ApiModelProperty("登录token")
    private String token;

    @ApiModelProperty("token header头")
    private String tokenName;
}
