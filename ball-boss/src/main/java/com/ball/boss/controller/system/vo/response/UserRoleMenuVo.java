package com.ball.boss.controller.system.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@ApiModel(description = "用户角色和菜单信息")
public class UserRoleMenuVo {
    @ApiModelProperty("菜单信息")
    private List<MenuResponseVo> menus;

    @ApiModelProperty("角色信息")
    private List<RoleResponseVo> roles;
}
