package com.ball.boss.controller.system.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "用户修改密码请求信息")
public class UserPasswordRequestVo {
    @ApiModelProperty("原密码")
    @NotBlank(message = "原密码不可为空")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不可为空")
    private String newPassword;

    /**
     * 新旧密码是否一致
     * @return  -- true表示不一致
     */
    public boolean isNotSame() {
        return !oldPassword.equals(newPassword);
    }
}
