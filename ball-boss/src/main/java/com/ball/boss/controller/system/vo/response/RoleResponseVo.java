package com.ball.boss.controller.system.vo.response;

import com.ball.boss.controller.system.vo.RoleBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "角色响应信息", value = "角色响应信息")
public class RoleResponseVo extends RoleBaseVo {
    @ApiModelProperty("角色编号")
    private String roleId;

    @ApiModelProperty("锁定标志 1锁定 0激活")
    private String locked;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
