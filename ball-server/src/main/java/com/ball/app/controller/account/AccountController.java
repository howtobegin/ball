package com.ball.app.controller.account;

import com.ball.app.controller.account.vo.AccountResp;
import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.exception.BizErrCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author littlehow
 */
@Api(tags = "账户信息")
@RestController
@RequestMapping("/app/account")
public class AccountController {
    @Autowired
    IUserAccountService iUserAccountService;


    @ApiOperation("获取用户账户信息")
    @GetMapping("get")
    public AccountResp get() {

        UserAccount account = iUserAccountService.lambdaQuery().eq(UserAccount::getUserId, UserContext.getUserNo()).one();
        BizAssert.notNull(account, BizErrCode.DATA_ERROR);
        AccountResp resp = BeanUtil.copy(account, AccountResp.class);
        return resp;
    }
}
