package com.ball.boss.controller.system.vo.response;

import com.ball.boss.controller.system.vo.MenuBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "菜单响应信息")
public class MenuResponseVo extends MenuBaseVo {
    @ApiModelProperty("菜单编号")
    private String menuId;
}
