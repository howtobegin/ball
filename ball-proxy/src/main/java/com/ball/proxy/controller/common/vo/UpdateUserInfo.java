package com.ball.proxy.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author JimChery
 * @since 2022-11-07 16:20
 */
@Setter
@Getter
public class UpdateUserInfo extends UserNoReq {

    @ApiModelProperty("状态 1:启用 0:禁用")
    private Integer status;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("信用额度")
    private BigDecimal balance;

    public boolean invalid() {
        return status == null && userName == null && balance == null;
    }

    public boolean hasStatus() {
        return status != null
                && (status == 1 || status == 0);
    }

    public boolean hasUserName() {
        return StringUtils.hasText(userName);
    }

    public boolean hasBalance() {
        return balance != null && balance.compareTo(BigDecimal.ZERO) >= 0;
    }
}
