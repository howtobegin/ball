package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author JimChery
 * @since 2022-11-02 14:24
 */
@Setter
@Getter
public class ProxyDetailResp {
    @ApiModelProperty("代理用户编号")
    private Long userNo;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("登入账号")
    private String loginAccount;

    @ApiModelProperty("名称")
    private String userName;

    @ApiModelProperty("最后修改密码时间")
    private Long changePasswordTime;

    @ApiModelProperty("上级代理用户编号")
    private Long proxyUserId;

    @ApiModelProperty("上级代理账号")
    private String proxyAccount;

    @ApiModelProperty("额度模式 RECOVERY:自动恢复 BALANCE:余额浮动")
    private String balanceMode;

    @ApiModelProperty("用户状态 1:正常 0:锁定")
    private Integer status;

    @ApiModelProperty("最后登录时间")
    private Long lastLogin;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("周期新增会员数")
    private Integer periodUserCount;

}
