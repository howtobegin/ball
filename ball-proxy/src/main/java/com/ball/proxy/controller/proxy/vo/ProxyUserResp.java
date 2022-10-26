package com.ball.proxy.controller.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author JimChery
 */
@Setter
@Getter
public class ProxyUserResp {
    @ApiModelProperty("用户编号")
    private Long userNo;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("登入账号")
    private String loginAccount;

    @ApiModelProperty("名称")
    private String userName;

    @ApiModelProperty("修改密码标志")
    private Integer changePasswordFlag;

    @ApiModelProperty("额度模式 RECOVERY:自动恢复 BALANCE:余额浮动")
    private String balanceMode;

    @ApiModelProperty("用户状态 1:正常 0:锁定")
    private Integer status;

    @ApiModelProperty("会员人数")
    public Integer userCount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public void setId(Long id) {
        userNo = id;
    }
}
