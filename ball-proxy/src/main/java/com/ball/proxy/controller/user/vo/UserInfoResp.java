package com.ball.proxy.controller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author JimChery
 * @since 2022-10-31 19:19
 */
@Setter
@Getter
public class UserInfoResp {
    @ApiModelProperty("用户编号")
    private Long userNo;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("登入账号")
    private String loginAccount;

    @ApiModelProperty("名称")
    private String userName;

    @ApiModelProperty("代理用户编号")
    private Long proxyUserId;

    @ApiModelProperty("额度模式 RECOVERY:自动恢复 BALANCE:余额浮动")
    private String balanceMode;

    @ApiModelProperty("用户状态 1:正常 0:锁定")
    private Integer status;

    @ApiModelProperty("最后登录时间")
    private Long lastLogin;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("信用额度")
    private BigDecimal balance;

    @ApiModelProperty("货币")
    private String currency;

    @ApiModelProperty("密码修改时间")
    private Long changePasswordTime;

    public void setId(Long id) {
        userNo = id;
    }
}
