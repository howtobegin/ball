package com.ball.proxy.controller.account;

import com.ball.base.context.UserContext;
import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.BizAssetAdjustmentOrder;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.account.vo.AccountModifyReq;
import com.ball.proxy.controller.account.vo.AccountModifyResp;
import com.ball.proxy.controller.account.vo.AccountResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Api(tags = "账户信息")
@RestController
@RequestMapping("/boss/account")
public class AccountController {
    @Autowired
    IUserAccountService iUserAccountService;

    @Autowired
    IBizAssetAdjustmentOrderService iBizAssetAdjustmentOrderService;


    @ApiOperation("获取用户账户信息")
    @RequestMapping(value = "get",method = {RequestMethod.GET,RequestMethod.POST})
    public AccountResp get() {
        UserAccount account = iUserAccountService.lambdaQuery().eq(UserAccount::getUserId, UserContext.getUserNo()).one();
        BizAssert.notNull(account, BizErrCode.DATA_ERROR);
        AccountResp resp = BeanUtil.copy(account, AccountResp.class);
        resp.setAvailableAmount(resp.getBalance().subtract(resp.getFreezeAmount()));
        return resp;
    }



    @ApiOperation("查询额度修改记录")
    @RequestMapping(value = "modifyRecord",method = {RequestMethod.GET,RequestMethod.POST})
    public PageResult<AccountModifyResp> modifyRecord(@RequestBody @Valid AccountModifyReq req) {
        LocalDateTime time = LocalDateTime.now().minusDays(req.getDays());
        PageResult<BizAssetAdjustmentOrder> result = iBizAssetAdjustmentOrderService.pageQuery(
                iBizAssetAdjustmentOrderService.lambdaQuery().eq(BizAssetAdjustmentOrder::getUserNo,UserContext.getUserNo())
                        .ge(BizAssetAdjustmentOrder::getCreateTime, time),req);


        return new PageResult<AccountModifyResp>(result.getRows().stream().map(po->{
            AccountModifyResp resp = new AccountModifyResp();
            resp.setCreateTime(po.getCreateTime());
            resp.setUserId(po.getUserNo());
            resp.setNewBalance(po.getNewBalance());
            resp.setOldBalance(po.getOldBalance());
            if (po.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                resp.setDeposit(po.getAmount());
                resp.setWithdraw(BigDecimal.ZERO);
            } else {
                resp.setDeposit(BigDecimal.ZERO);
                resp.setWithdraw(po.getAmount().abs());
            }

            return resp;
        }).collect(Collectors.toList()), result.getTotalNum(), result.getPageIndex(),result.getPageSize());
    }
}
