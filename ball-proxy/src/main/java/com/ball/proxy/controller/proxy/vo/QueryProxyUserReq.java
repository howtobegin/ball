package com.ball.proxy.controller.proxy.vo;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author JimChery
 */
@Setter
@Getter
public class QueryProxyUserReq extends Paging {
    @ApiModelProperty("账号搜索")
    private String account;

    @ApiModelProperty("额度模式 RECOVERY:自动恢复 BALANCE:余额浮动")
    private String balanceMode;

    @ApiModelProperty("状态 1:正常 0:锁定")
    private Integer status;

    public boolean hasAccount() {
        return StringUtils.hasText(account);
    }

    public boolean hasBalanceMode() {
        return StringUtils.hasText(balanceMode);
    }

    public boolean hasStatus() {
        return status != null;
    }
}
