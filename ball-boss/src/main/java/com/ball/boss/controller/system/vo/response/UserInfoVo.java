package com.ball.boss.controller.system.vo.response;

import com.ball.boss.controller.system.vo.UserBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户信息")
public class UserInfoVo extends UserBaseVo {
    @ApiModelProperty("登录账户(最长30位)")
    private String account;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
