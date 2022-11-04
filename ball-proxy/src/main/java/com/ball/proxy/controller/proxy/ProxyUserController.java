package com.ball.proxy.controller.proxy;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.model.DecimalHandler;
import com.ball.base.model.PageResult;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.bo.ProxyChildrenBalance;
import com.ball.biz.user.bo.ProxyStatistics;
import com.ball.biz.user.bo.ProxyUserInfo;
import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.mapper.ext.UserExtMapper;
import com.ball.biz.user.proxy.ProxyUserService;
import com.ball.biz.user.service.IUserExtendService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.config.HttpSessionConfig;
import com.ball.proxy.controller.proxy.vo.*;
import com.ball.proxy.interceptor.LoginInterceptor;
import com.ball.proxy.service.ProxyUserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author JimChery
 */
@Api(tags = "代理商管理")
@RestController
@RequestMapping("/proxy/")
public class ProxyUserController {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ProxyUserService proxyUserService;

    @Autowired
    private ProxyUserOperationService proxyUserOperationService;

    @Autowired
    private UserExtMapper userExtMapper;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private ISettlementPeriodService settlementPeriodService;

    @Autowired
    private IUserExtendService userExtendService;

    @ApiOperation("登录")
    @PostMapping("login")
    public LoginResp login(@RequestBody @Valid LoginReq req, HttpServletRequest request) {
        UserTypeEnum typeEnum = UserTypeEnum.proxyOf(req.getUserType());
        UserInfo userInfo = proxyUserService.login(request.getSession().getId(), req.getAccount(), req.getPassword(), typeEnum.v);
        request.getSession().setAttribute(LoginInterceptor.SESSION_USER, "" + userInfo.getId());
        return new LoginResp().setLoginAccount(req.getAccount())
                .setToken(HttpSessionConfig.getToken(request))
                .setTokenName(HttpSessionConfig.TOKEN_NAME)
                .setUserNo(userInfo.getId())
                .setChangePasswordFlag(userInfo.getChangePasswordFlag())
                .setChangeAccountFlag(userInfo.getAccount().equals(userInfo.getLoginAccount()) ? YesOrNo.NO.v : YesOrNo.NO.v);
    }


    @ApiOperation("登出接口")
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userNo = (String) session.getAttribute(LoginInterceptor.SESSION_USER);
        session.removeAttribute(LoginInterceptor.SESSION_USER);
        if (userNo != null) {
            proxyUserService.logout(Long.valueOf(userNo));
        }
        session.invalidate();
    }

    @ApiOperation("添加代理商")
    @PostMapping("addProxy")
    public void addProxy(@RequestBody @Valid AddProxyReq req) {
        proxyUserOperationService.addProxyUser(req);
    }

    @ApiOperation("添加代理一(临时接口)")
    @PostMapping("addProxyOne")
    public void addProxyOne(@RequestBody @Valid AddProxyReq req) {
        proxyUserOperationService.addProxyOne(req);
    }

    @ApiOperation("查询代理商")
    @PostMapping("queryProxy")
    public PageResult<ProxyUserResp> query(@RequestBody @Valid QueryProxyUserReq req) {
        Long userNo = UserContext.getUserNo();
        PageResult<ProxyUserResp> resp = userInfoService.pageQuery(userInfoService.lambdaQuery()
                .eq(UserInfo::getProxyUserId, userNo)
                .eq(req.hasBalanceMode(), UserInfo::getBalanceMode, req.getBalanceMode())
                .eq(req.hasStatus(), UserInfo::getStatus, req.getStatus())
                .gt(UserInfo::getUserType, UserTypeEnum.PROXY_ONE.v)
                .like(req.hasAccount(), UserInfo::getAccount, req.getAccount()), req, ProxyUserResp.class);
        if (resp.hasResult()) {
            List<Long> proxyUids = resp.stream().map(ProxyUserResp::getUserNo).collect(Collectors.toList());
            List<ProxyUserInfo> proxyUserInfos = userExtMapper.proxyUser(proxyUids);
            Map<Long, Integer> proxyUserCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(proxyUserInfos)) {
               proxyUserInfos.forEach(o -> proxyUserCountMap.put(o.getProxyUid(), o.getUserCount()));
            }
            DecimalHandler handler = DecimalHandler.instance();
            // 计算余额和币种
            List<UserAccount> userAccounts = userAccountService.queryList(proxyUids);
            Map<Long, UserAccount> userAccountMap = userAccounts.stream().collect(Collectors.toMap(UserAccount::getUserId, Function.identity()));
            resp.foreach(o -> {
                if (o.getAccount().equals(o.getLoginAccount())) {
                    o.setLoginAccount(null);
                }
                o.setUserCount(proxyUserCountMap.get(o.getUserNo()));
                UserAccount account = userAccountMap.get(o.getUserNo());
                if (account != null) {
                    o.setBalance(handler.add(account.getBalance()).subtract(account.getFreezeAmount()).getValue());
                }
                handler.clear();
            });
        }

        return resp;
    }

    @ApiOperation("查询所有代理(下拉选择用)")
    @PostMapping("querySummary")
    public List<ProxyUserSummaryResp> querySummary() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery().eq(UserInfo::getProxyUserId, UserContext.getUserNo())
                .gt(UserInfo::getUserType, UserTypeEnum.PROXY_ONE.v).list();
        if (CollectionUtils.isEmpty(userInfos)) {
            return new ArrayList<>();
        } else {
            return userInfos.stream().map(o -> {
                ProxyUserSummaryResp resp = new ProxyUserSummaryResp();
                resp.setAccount(o.getAccount());
                resp.setBalanceMode(o.getBalanceMode());
                resp.setUserNo(o.getId());
                return resp;
            }).collect(Collectors.toList());
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("changePassword")
    public void changePassword(@RequestBody @Valid ChangePasswordReq req) {
        // 校验密码合法性
        BizAssert.isTrue(PasswordUtil.checkValid(req.getNewPassword()), BizErrCode.USER_PASSWORD_INVALID);
        proxyUserOperationService.updatePassword(UserContext.getUserNo(), req.getOldPassword(), req.getNewPassword());
    }

    @ApiOperation("首次修改密码")
    @PostMapping("changePasswordFirst")
    public void changePasswordFirst(@RequestBody @Valid FirstChangePasswordReq req) {
        // 校验密码合法性
        BizAssert.isTrue(PasswordUtil.checkValid(req.getPassword()), BizErrCode.USER_PASSWORD_INVALID);
        proxyUserOperationService.updatePasswordFirst(UserContext.getUserNo(), req.getPassword());
    }

    @ApiOperation("修改登入账号")
    @PostMapping("changeLoginAccount")
    public void changeLoginAccount(@RequestBody @Valid LoginAccountReq req) {
        req.valid();
        userInfoService.changeLogin(UserContext.getUserNo(), req.getLoginAccount());
    }

    @ApiOperation("检查登入账号是否可用")
    @PostMapping("checkLoginAccount")
    public boolean checkLoginAccount(@RequestBody @Valid LoginAccountReq req) {
        int count = userInfoService.lambdaQuery().eq(UserInfo::getAccount, req.getLoginAccount())
                .gt(UserInfo::getUserType, UserTypeEnum.GENERAL.v).count();
        if (count > 0) {
            return false;
        }
        count = userInfoService.lambdaQuery().eq(UserInfo::getLoginAccount, req.getLoginAccount())
                .gt(UserInfo::getUserType, UserTypeEnum.GENERAL.v).count();
        return count <= 0;
    }

    @ApiOperation("查询代理详细信息")
    @PostMapping("queryProxyDetail")
    public ProxyDetailResp queryProxyDetail(@RequestBody @Valid ProxyDetailReq req) {
        UserInfo userInfo;
        // 当前登陆用户必须是代理3自己或者自己的上级
        if (UserTypeEnum.PROXY_THREE.isMe(UserContext.getUserType())) {
            userInfo = proxyUserService.getByUid(UserContext.getUserNo());
        } else {
            BizAssert.isTrue(req.hasProxyUid(), BizErrCode.PARAM_ERROR_DESC, "proxyUid");
            userInfo = proxyUserService.getByUid(req.getProxyUid());
            BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        }
        ProxyDetailResp resp = BeanUtil.copy(userInfo, ProxyDetailResp.class);
        resp.setUserNo(userInfo.getId());
        // 查询周期内新增会员
        SettlementPeriod period = settlementPeriodService.currentPeriod();
        if (period != null) {
            Integer count = userExtMapper.selectProxyStatisticsPeriod(userInfo.getId(), period.getStartDate(), period.getEndDate());
            resp.setPeriodUserCount(count);
        }
        return resp;
    }

    @ApiOperation("查询代理下级会员信息")
    @PostMapping("queryProxyChildrenStatistics")
    public List<ProxyChildrenResp> queryProxyChildrenStatistics(@RequestBody @Valid ProxyDetailReq req) {
        UserInfo userInfo = getProxyUid(req.getProxyUid());
        List<ProxyStatistics> proxyStatistics = userExtMapper.selectProxyStatistics(userInfo.getId());
        if (CollectionUtils.isEmpty(proxyStatistics)) {
            return getChildrenInfo(userInfo.getBalanceMode(), null, null);
        }
        List<ProxyChildrenBalance> balances = userExtMapper.selectProxyChildrenBalance(userInfo.getId());
        return getChildrenInfo(userInfo.getBalanceMode(), proxyStatistics, balances);
    }

    @ApiOperation("查询代理分成信息")
    @PostMapping("queryProxyRate")
    public ProxyRateResp queryProxyRate(@RequestBody @Valid ProxyDetailReq req) {
        UserInfo userInfo = getProxyUid(req.getProxyUid());
        UserExtend userExtend = userExtendService.getByUid(userInfo.getId());
        ProxyRateResp resp = new ProxyRateResp();
        resp.setProxyUid(userInfo.getId());
        resp.setProxyRate(userExtend.getProxyRate());
        resp.setTotalProxyRate(userExtend.getTotalProxyRate());
        return resp;
    }

    private UserInfo getProxyUid(Long proxyUid) {
        UserInfo userInfo;
        // 当前登陆用户必须是代理3自己或者自己的上级
        if (UserTypeEnum.PROXY_THREE.isMe(UserContext.getUserType())) {
            userInfo = new UserInfo().setId(UserContext.getUserNo())
                    .setBalanceMode(UserContext.getBalanceMode());
        } else {
            BizAssert.notNull(proxyUid, BizErrCode.PARAM_ERROR_DESC, "proxyUid");
            userInfo = proxyUserService.getByUid(proxyUid);
            BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        }
        return userInfo;
    }

    /**
     * 构建不同状态下的统计
     * 新增状态此处必须跟着新增，否则会出现空指针异常
     * @param allowanceMode     - 额度模式
     * @param statistics        - 会员数统计
     * @param balances          - 信用额度统计
     * @return -
     */
    private List<ProxyChildrenResp> getChildrenInfo(String allowanceMode,
                                                    List<ProxyStatistics> statistics, List<ProxyChildrenBalance> balances) {
        List<ProxyChildrenResp> resps = new ArrayList<>();
        Map<Integer, ProxyChildrenResp> proxyChildrenRespMap = new HashMap<>();
        ProxyChildrenResp yes = new ProxyChildrenResp().setBalance(BigDecimal.ZERO)
                .setStatus(YesOrNo.YES.v).setUserCount(0);
        resps.add(yes);
        proxyChildrenRespMap.put(YesOrNo.YES.v, yes);
        ProxyChildrenResp no = new ProxyChildrenResp().setBalance(BigDecimal.ZERO)
                .setStatus(YesOrNo.NO.v).setUserCount(0);
        resps.add(no);
        proxyChildrenRespMap.put(YesOrNo.NO.v, no);
        if (CollectionUtils.isEmpty(statistics)) {
            return resps;
        }
        statistics.forEach(o -> proxyChildrenRespMap.get(o.getStatus()).setUserCount(o.getUserCount()));
        if (CollectionUtils.isEmpty(balances)) {
            return resps;
        }
        balances.forEach(o -> {
            ProxyChildrenResp resp = proxyChildrenRespMap.get(o.getStatus());
            if (AllowanceModeEnum.BALANCE.name().equals(allowanceMode)) {
                resp.setBalance(o.getBalance());
            } else {
                resp.setBalance(o.getAllowance());
            }
        });
        return resps;
    }
}
