package com.ball.proxy.controller.proxy;

import com.ball.base.model.enums.YesOrNo;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.proxy.ProxyUserService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.config.HttpSessionConfig;
import com.ball.proxy.controller.proxy.vo.AddProxyReq;
import com.ball.proxy.controller.proxy.vo.LoginReq;
import com.ball.proxy.controller.proxy.vo.LoginResp;
import com.ball.proxy.interceptor.LoginInterceptor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author littlehow
 */
@Api(tags = "代理用户登录")
@RestController
@RequestMapping("/proxy/")
public class ProxyUserController {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ProxyUserService proxyUserService;

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
        // todo littlehow
    }
}
