package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "菜单信息")
public class MenuBaseVo {
    @ApiModelProperty("菜单名称(最长50)")
    @NotBlank(message = "菜单名称不可为空")
    @Length(max = 50, message = "菜单名称不可超过50字符")
    private String menuName;

    @ApiModelProperty("上级菜单编号")
    private String parentId;

    @ApiModelProperty("节点图标CSS类名")
    private String iconCls;

    @ApiModelProperty("展开状态(1:展开;0:收缩) ")
    @Pattern(regexp = "[01]", message = "展开状态只能为1或0")
    private String expanded;

    @ApiModelProperty("请求地址(最长200)")
    @Length(max = 200, message = "请求地址不可超过200字符")
    private String request;

    @ApiModelProperty("菜单级别(0:树枝节点;1:叶子节点;2:按钮级别)")
    @Pattern(regexp = "[012]", message = "菜单级别只能为0、1或2")
    private String menuLevel;

    @ApiModelProperty("排序号")
    @NotNull(message = "排序号不可为空")
    @Min(value = 1, message = "排序号最小从1开始")
    private Integer sortNo;

    @ApiModelProperty("备注(最长200)")
    @Length(max = 200, message = "备注不可超过200字符")
    private String remark;

    @ApiModelProperty("节点图标")
    private String icon;

    @ApiModelProperty("菜单类型(1:系统菜单;0:业务菜单)")
    @Pattern(regexp = "[01]", message = "菜单类型只能为1或0")
    private String menuType;
}
