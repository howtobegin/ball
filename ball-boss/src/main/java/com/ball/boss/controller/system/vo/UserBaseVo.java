package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "用户基础信息")
public class UserBaseVo {

    @ApiModelProperty("登录账户(最长30位)")
    @NotBlank(message = "登录账户不可为空")
    @Length(max = 30, message = "登录账户不可超过30字符")
    private String account;

    @ApiModelProperty("用户名(最长30位)")
    @NotBlank(message = "用户名称不可为空")
    @Length(max = 30, message = "用户名不可超过30字符")
    private String userName;

    @ApiModelProperty("性别(0:未知;1:男;2:女)")
    @Pattern(regexp = "[012]", message = "性别只能是0,1,2三个值")
    private String sex;

    @ApiModelProperty("锁定标志(1:锁定;0:激活)")
    @Pattern(regexp = "[10]", message = "锁定标志只能为0或1")
    private String locked;

    @ApiModelProperty("备注(最长50)")
    @Length(max = 50, message = "备注不可超过50字符")
    private String remark;

    @ApiModelProperty("人员类型(1:业务员;2:管理员;) ")
    @Pattern(regexp = "[12]", message = "用户类型只能是1或2")
    private String userType;

    @ApiModelProperty("手机区号")
    private String mobileArea;

    @ApiModelProperty("手机号")
    @Pattern(regexp = "\\d{11}", message = "手机号格式不正确")
    private String mobilePhone;
}
