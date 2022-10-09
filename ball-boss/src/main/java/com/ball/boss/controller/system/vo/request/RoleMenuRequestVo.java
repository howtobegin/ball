package com.ball.boss.controller.system.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(description = "角色菜单请求信息")
public class RoleMenuRequestVo {
    @ApiModelProperty("角色编号")
    @NotBlank(message = "角色编号不可为空")
    private String roleId;

    @ApiModelProperty("菜单编号集合")
    @NotEmpty(message = "菜单编号不可为空")
    private List<String> menuIds;
}
