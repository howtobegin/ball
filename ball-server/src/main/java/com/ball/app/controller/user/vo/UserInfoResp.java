package com.ball.app.controller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author littlehow
 */
@Setter
@Getter
public class UserInfoResp {
    @ApiModelProperty("用户编号")
    private Long userNo;

    @ApiModelProperty("登入账号")
    private String loginAccount;

    @ApiModelProperty("名称")
    private String userName;

    @ApiModelProperty("修改密码标志 1:已修改 0:未修改")
    private Integer changePasswordFlag;

    @ApiModelProperty("账号修改标志 1:已修改 0:未修改")
    private Integer changeAccountFlag;

    @ApiModelProperty("最后登录时间")
    private Long lastLogin;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
