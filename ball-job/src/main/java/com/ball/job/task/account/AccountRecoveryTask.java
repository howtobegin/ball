package com.ball.job.task.account;

import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.bet.order.job.OrderAwardService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.job.base.BaseBizTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 额度恢复任务
 * @author ab
 * @date 2022/10/20 下午6:23
 */
@Slf4j
@Component
public class AccountRecoveryTask extends BaseBizTask {
    @Autowired
    private IUserAccountService iUserAccountService;

    @Value("${job.account.recovery.enabled:true}")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Value("${job.account.recovery.expression:0 * * */1 * ?}")
    public void setExpression(String expression) {
        super.setExpression(expression);
    }

    @Override
    public void execute() {
        iUserAccountService.lambdaUpdate().setSql("balance=allowance")
                .eq(UserAccount::getAllowanceMode, AllowanceModeEnum.RECOVERY.name())
                .eq(UserAccount::getUserType, UserTypeEnum.GENERAL.v+"")
                .update();
    }
}
