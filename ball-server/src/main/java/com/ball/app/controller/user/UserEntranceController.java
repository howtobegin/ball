package com.ball.app.controller.user;

import com.ball.app.config.HttpSessionConfig;
import com.ball.app.controller.user.vo.LoginReq;
import com.ball.app.controller.user.vo.LoginResp;
import com.ball.app.interceptor.LoginInterceptor;
import com.ball.base.model.enums.YesOrNo;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
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
@Api(tags = "用户登录")
@RestController
@RequestMapping("/app/entrance/")
public class UserEntranceController {

    @Autowired
    private IUserInfoService userInfoService;

    @ApiOperation("登录")
    @PostMapping("login")
    public LoginResp login(@RequestBody @Valid LoginReq req, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.login(request.getSession().getId(), req.getAccount(), req.getPassword());
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
            userInfoService.logout(Long.valueOf(userNo));
        }
        session.invalidate();
    }
}
