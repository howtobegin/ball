package com.ball.proxy.controller.user.vo;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author JimChery
 * @since 2022-10-31 19:25
 */
@Setter
@Getter
public class QueryUserReq extends Paging {
    @ApiModelProperty("代理用户编号,代理2时必传")
    private Long proxyUid;

    @ApiModelProperty("状态 1:正常 0:锁定")
    private Integer status;

    @ApiModelProperty("账号")
    private String account;

    public boolean hasProxy() {
        return proxyUid != null;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasAccount() {
        return StringUtils.hasText(account);
    }
}
