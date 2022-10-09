package com.ball.boss.service.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Builder
@Getter
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {
    public static final long serialVersionUID = 1L;

    @ApiModelProperty("用户编号")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("账户")
    private String account;
    @ApiModelProperty("手机区号，没有加号")
    private String mobileArea;
    @ApiModelProperty("手机号")
    private String mobilePhone;
    @ApiModelProperty("是否绑定了google验证码 true:已绑定 false:未绑定")
    private Boolean googleFlag;

    public void setGoogleFlag(Boolean flag) {
        this.googleFlag = flag;
    }
}
