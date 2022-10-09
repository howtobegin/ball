package com.ball.boss.controller.system.vo.request;

import com.ball.boss.controller.system.vo.UserBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "用户修改信息")
public class UserUpdateVo extends UserBaseVo {
    @ApiModelProperty("用户编号")
    @NotBlank(message = "用户编号不可为空")
    private String userId;
}
