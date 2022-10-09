package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "角色信息")
public class RoleBaseVo {

    @ApiModelProperty("角色名称(最长50)")
    @NotBlank(message = "角色名称不可为空")
    @Length(max = 50, message = "角色名称不可超过50字符")
    private String roleName;

    @ApiModelProperty("角色类型(1:业务角色;2:管理角色)")
    @Pattern(regexp = "[12]", message = "角色类型只能为1或2")
    private String roleType;

    @ApiModelProperty("备注(最长50)")
    @Length(max = 50, message = "角色备注不可超过50字符")
    private String remark;
}
