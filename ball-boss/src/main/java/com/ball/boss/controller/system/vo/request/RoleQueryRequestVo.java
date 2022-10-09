package com.ball.boss.controller.system.vo.request;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "角色分页查询请求")
public class RoleQueryRequestVo extends Paging {
    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色描述")
    private String remark;
}
