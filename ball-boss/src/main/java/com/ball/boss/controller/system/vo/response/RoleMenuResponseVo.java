package com.ball.boss.controller.system.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "角色响应信息")
public class RoleMenuResponseVo extends RoleResponseVo {
    @ApiModelProperty("权限信息")
    private List<MenuResponseVo> menus;
}
