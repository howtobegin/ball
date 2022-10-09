package com.ball.boss.controller.system.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(description = "用户角色请求信息")
public class UserRoleRequestVo {
    @ApiModelProperty("用户编号")
    @NotBlank(message = "用户编号不可为空")
    private String userId;

    @ApiModelProperty("角色编号集合")
    @NotEmpty(message = "角色编号不可为空")
    private List<String> roleIds;
}
