package com.ball.proxy.controller.account;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.base.util.DateUtil;
import com.ball.biz.account.entity.BizAssetAdjustmentOrder;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.mapper.ext.UserExtMapper;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.account.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Api(tags = "账户信息")
@RestController
@RequestMapping("/proxy/account")
public class AccountController {
    @Autowired
    IUserAccountService iUserAccountService;

    @Autowired
    IUserInfoService iUserInfoService;

    @Autowired
    IBizAssetAdjustmentOrderService iBizAssetAdjustmentOrderService;

    @Autowired
    ISettlementPeriodService iSettlementPeriodService;

    @Autowired
    private UserExtMapper userExtMapper;


    @ApiOperation("获取用户账户信息")
    @RequestMapping(value = "get",method = {RequestMethod.GET,RequestMethod.POST})
    public AccountResp get() {
        UserAccount account = iUserAccountService.lambdaQuery().eq(UserAccount::getUserId, UserContext.getUserNo()).one();
        BizAssert.notNull(account, BizErrCode.DATA_ERROR);
        AccountResp resp = BeanUtil.copy(account, AccountResp.class);
        resp.setAvailableAmount(resp.getBalance().subtract(resp.getFreezeAmount()));
        return resp;
    }

    @ApiOperation("获取用户账户信息")
    @RequestMapping(value = "getById",method = {RequestMethod.GET,RequestMethod.POST})
    public AccountResp getById(@RequestBody @Valid AccountGetReq req) {
        if (!req.getUserNo().equals(UserContext.getUserNo())) {
            UserInfo userInfo = iUserInfoService.getByUid(req.getUserNo());
            BizAssert.notNull(userInfo, BizErrCode.USER_NOT_EXISTS);
            BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(),UserContext.getUserNo()),BizErrCode.USER_ACCOUNT_RULE_ERROR );
        }

        UserAccount account = iUserAccountService.lambdaQuery().eq(UserAccount::getUserId, req.getUserNo()).one();
        BizAssert.notNull(account, BizErrCode.DATA_ERROR);
        AccountResp resp = BeanUtil.copy(account, AccountResp.class);
        resp.setAvailableAmount(resp.getBalance().subtract(resp.getFreezeAmount()));
        return resp;
    }


    @ApiOperation("修改信用额度")
    @PostMapping(value = "updateAllowance")
    public void updateAllowance(@RequestBody @Valid AccountAllowanceUpdateReq req) {
        UserInfo userInfo = iUserInfoService.getByUid(req.getUserNo());
        BizAssert.notNull(userInfo, BizErrCode.USER_NOT_EXISTS);
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(),UserContext.getUserNo()),BizErrCode.USER_ACCOUNT_RULE_ERROR );
        UserInfo operator = iUserInfoService.getByUid(UserContext.getUserNo());
        iBizAssetAdjustmentOrderService.updateAllowance(req.getUserNo(),req.getAllowance(),userInfo.getProxyUserId(),operator);
    }

    @ApiOperation("查询额度修改记录")
    @RequestMapping(value = "modifyRecord",method = {RequestMethod.GET,RequestMethod.POST})
    public PageResult<AccountModifyResp> modifyRecord(@RequestBody @Valid AccountModifyReq req) {
        LocalDateTime timeStart = LocalDateTime.now().truncatedTo(ChronoUnit.MONTHS).minusMonths(req.getMonths());;
        LocalDateTime timeEnd = timeStart.plusMonths(1);
        PageResult<BizAssetAdjustmentOrder> result = iBizAssetAdjustmentOrderService.pageQuery(
                iBizAssetAdjustmentOrderService.lambdaQuery().eq(BizAssetAdjustmentOrder::getUserNo,UserContext.getUserNo())
                        .between(BizAssetAdjustmentOrder::getCreateTime, timeStart, timeEnd),req);


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

    @ApiOperation("账户概况")
    @PostMapping(value = "summary")
    public AccountSummaryResp summary() {
        UserAccount account = iUserAccountService.lambdaQuery().eq(UserAccount::getUserId, UserContext.getUserNo()).one();
        BizAssert.notNull(account, BizErrCode.DATA_ERROR);
        AccountSummaryResp resp = BeanUtil.copy(account, AccountSummaryResp.class);
        SettlementPeriod period = iSettlementPeriodService.currentPeriod();
        if (period != null) {
            resp.setCurrentPeriod(String.format("%s - %s", DateUtil.formatDateHasSlash(period.getStartDate()), DateUtil.formatDateHasSlash(period.getEndDate())));
            LocalDateTime now = LocalDateTime.now();
            resp.setPeriodLeftDays(Math.max(0,Duration.between(now,period.getEndDate()).toDays()));
            resp.setPeriodFinishedDays(Math.min(Duration.between(period.getStartDate(),now).toDays(),
                    Duration.between(period.getStartDate(),period.getEndDate()).toDays()));
            // 统计会员数
            Integer userCount = userExtMapper.selectProxyStatisticsPeriod(UserContext.getUserNo(),
                    period.getStartDate(), period.getEndDate());
            resp.setPeriodUserCount(userCount);
            resp.setPeriod(BeanUtil.copy(period,SettlementPeriodResp.class));
        }
        UserInfo userInfo = iUserInfoService.getByUid(UserContext.getUserNo());
        resp.setLastLoginTime(userInfo.getLastLogin());
        resp.setChangePasswordTime(userInfo.getChangePasswordTime());

        return resp;
    }

}
