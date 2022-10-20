package com.ball.proxy.controller.proxy.vo;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author littlehow
 */
@Setter
@Getter
public class QueryProxyUserReq extends Paging {
    @ApiModelProperty("账号搜索")
    private String account;

    @ApiModelProperty("额度模式 RECOVERY:自动恢复 BALANCE:余额浮动")
    private String balanceMode;

    public boolean hasAccount() {
        return StringUtils.hasText(account);
    }

    public boolean hasBalanceMode() {
        return StringUtils.hasText(balanceMode);
    }
}
