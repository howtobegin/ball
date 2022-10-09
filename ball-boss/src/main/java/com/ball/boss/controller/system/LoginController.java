package com.ball.boss.controller.system;


import com.ball.base.util.EncryptUtil;
import com.ball.base.util.SaltBase64Util;
import com.ball.boss.config.HttpSessionConfig;
import com.ball.boss.controller.system.vo.request.LoginRequestVo;
import com.ball.boss.controller.system.vo.response.LoginVo;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.interceptor.LoginInterceptor;
import com.ball.boss.service.system.LoginService;
import com.ball.boss.service.system.UserService;
import com.ball.boss.service.system.model.UserInfo;
import com.ball.common.service.GoogleAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 登录
 */
@Api(tags = "登入登出接口")
@RestController
@RequestMapping("/boss/account")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private GoogleAuthService googleAuthService;

    @ApiOperation("登入接口")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginVo login(@RequestBody @Valid LoginRequestVo login, HttpServletRequest request) {

        BossUserInfo po = userService.queryByAccountAndPassword(login.getAccountId(), EncryptUtil.md5Plus(login.getPassword()));
        Assert.notNull(po, "用户名或密码错误");
        // 如果有google验证码需要验证
        if (StringUtils.hasText(po.getGoogleKey())) {
            Assert.notNull(login.getGoogleCode(), "谷歌验证码不可为空");
            googleAuthService.checkGoogleCode(SaltBase64Util.decode(po.getGoogleKey()), login.getGoogleCode());
        }
        // 再进行真实登录
        UserInfo userInfo = loginService.login(login.getAccountId(), login.getPassword(),
                request.getSession().getId());
        userInfo.setGoogleFlag(StringUtils.hasText(po.getGoogleKey()));
        request.getSession().setAttribute(LoginInterceptor.SESSION_USER, userInfo);
        return LoginVo.builder()
                .token(HttpSessionConfig.getToken(request))
                .userInfo(userInfo)
                .build();
    }

    @ApiOperation("检查踢出信息")
    @RequestMapping(value = "checkKickOut", method = { RequestMethod.POST, RequestMethod.GET })
    public void checkKickOut() {
        // do nothing
    }

    @ApiOperation("登出接口")
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(LoginInterceptor.SESSION_USER);
        session.removeAttribute(LoginInterceptor.SESSION_USER);
        if (userInfo != null) {
            loginService.logout(userInfo.getUserId());
        }
        session.invalidate();
    }

}
