package com.ball.boss.controller.system.vo.request;

import com.ball.boss.controller.system.vo.MenuBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "菜单修改信息")
public class MenuUpdateVo extends MenuBaseVo {
    @ApiModelProperty("菜单编号")
    @NotBlank(message = "菜单权限不可为空")
    private String menuId;
}
