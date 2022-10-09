package com.ball.boss.controller.system.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "用户锁定信息")
public class UserLockRequestVo {
    @ApiModelProperty("用户编号")
    @NotBlank(message = "用户编号不可为空")
    private String userId;

    @ApiModelProperty("锁定标志 1:锁定;0:激活")
    @NotBlank(message = "锁定标志不可为空")
    @Pattern(regexp = "[01]", message = "锁定标志只能是1或0")
    private String lockFlag;
}
