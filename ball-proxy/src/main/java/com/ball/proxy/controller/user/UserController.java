package com.ball.proxy.controller.user;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.model.DecimalHandler;
import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.common.vo.UserNoReq;
import com.ball.proxy.controller.user.vo.AddUserReq;
import com.ball.proxy.controller.user.vo.QueryUserReq;
import com.ball.proxy.controller.user.vo.UserInfoResp;
import com.ball.proxy.service.UserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author JimChery
 */
@Api(tags = "代理会员管理")
@RestController
@RequestMapping("/proxy/user/")
public class UserController {

    @Autowired
    private UserOperationService userOperationService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserAccountService userAccountService;

    @ApiOperation("添加会员")
    @PostMapping("add")
    public void add(@RequestBody @Valid AddUserReq req) {
        userOperationService.addUser(req);
    }

    @ApiOperation("查询会员列表")
    @PostMapping("queryList")
    public PageResult<UserInfoResp> queryList(@RequestBody @Valid QueryUserReq req) {
        // 如果当前用户是登2及以上，则必须加上代理编号
        if (UserTypeEnum.PROXY_THREE.isMe(UserContext.getUserType())) {
            req.setProxyUid(UserContext.getUserNo());
        } else {
            BizAssert.isTrue(req.hasProxy(), BizErrCode.PARAM_ERROR_DESC, "proxyUid");
        }
        PageResult<UserInfoResp> resp = userInfoService.pageQuery(userInfoService.lambdaQuery()
                .eq(UserInfo::getProxyUserId, req.getProxyUid())
                .eq(req.hasStatus(), UserInfo::getStatus, req.getStatus())
                .orderByDesc(UserInfo::getId), req, UserInfoResp.class);
        if (resp.hasResult()) {
            DecimalHandler handler = DecimalHandler.instance();
            // 计算余额和币种
            List<Long> userNos = resp.stream().map(UserInfoResp::getUserNo).collect(Collectors.toList());
            List<UserAccount> userAccounts = userAccountService.queryList(userNos);
            Map<Long, UserAccount> userAccountMap = userAccounts.stream().collect(Collectors.toMap(UserAccount::getUserId, Function.identity()));
            resp.foreach(o -> {
                UserAccount account = userAccountMap.get(o.getUserNo());
                if (account != null) {
                    o.setCurrency(account.getCurrency());
                    o.setBalance(handler.add(account.getBalance()).subtract(account.getFreezeAmount()).getValue());
                }
                handler.clear();
            });
        }
        return resp;
    }

    @ApiOperation("查询会员列表")
    @PostMapping("queryInfo")
    public UserInfoResp queryInfo(@RequestBody @Valid UserNoReq req) {
        UserInfo userInfo = userInfoService.getByUid(req.getUserNo());
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        UserAccount account = userAccountService.query(req.getUserNo());
        UserInfoResp resp = BeanUtil.copy(userInfo, UserInfoResp.class);
        resp.setCurrency(account.getCurrency());
        resp.setBalance(account.getBalance().subtract(account.getFreezeAmount()));
        return resp;
    }
}
