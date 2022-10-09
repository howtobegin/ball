package com.ball.boss.controller.system.vo.request;

import com.ball.boss.controller.system.vo.RoleBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "角色修改信息")
public class RoleUpdateVo extends RoleBaseVo {
    @ApiModelProperty("角色编号")
    @NotBlank(message = "角色编号不可为空")
    private String roleId;
}
