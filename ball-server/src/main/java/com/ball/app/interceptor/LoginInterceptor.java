package com.ball.app.interceptor;


import com.ball.base.context.AppLoginUser;
import com.ball.base.context.UserContext;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.Base64Util;
import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.biz.user.service.IUserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.ball.app.config.HttpSessionConfig.TOKEN_NAME;

public class LoginInterceptor implements HandlerInterceptor {
    public static final String SESSION_USER = "user";

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserLoginLogService userLoginLogService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        HttpSession session = httpServletRequest.getSession();
        String userId = (String) session.getAttribute(SESSION_USER);
        if (StringUtils.isEmpty(userId)) {
            String token = getToken(httpServletRequest);
            // 判断有无token
            if (StringUtils.hasText(token)) {
                userLoginLogService.checkKickOut(Base64Util.decode2String(token));
            }
            setUnauthorizedInfo(httpServletResponse);
            return false;
        }

        Long userNo = Long.valueOf(userId);
        UserInfo userInfo = userInfoService.getByUid(userNo);
        BizAssert.isTrue(YesOrNo.YES.isMe(userInfo.getStatus()), BizErrCode.USER_LOCKED);
        checkPasswordOrLogin(httpServletRequest, userInfo);
        List<UserInfo> proxy = userInfoService.getByProxyInfo(userInfo.getProxyInfo());
        for (UserInfo u : proxy) {
            BizAssert.isTrue(YesOrNo.YES.isMe(u.getStatus()), BizErrCode.USER_LOCKED);
        }
        UserContext.set(new AppLoginUser().setUserNo(userNo).setUserName(userInfo.getUserName())
            .setAccount(userInfo.getAccount()).setLoginAccount(userInfo.getLoginAccount())
                .setUserType(userInfo.getUserType())
        );
        TraceLogContext.setUserNo(userNo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //删除设置的上下文
        UserContext.clear();
    }

    private static String getToken(HttpServletRequest request) {
        String headerValue = request.getHeader(TOKEN_NAME);
        if (StringUtils.hasText(headerValue)) {
            return headerValue;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_NAME.equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 检测是否没有修改过密码或者没有修改过登入账户
     * @param request   -
     * @param userInfo  -  用户信息
     */
    private void checkPasswordOrLogin(HttpServletRequest request, UserInfo userInfo) {
        // todo JimChery 确认url做处理
    }

    /**
     * 设置未授权信息
     *
     * @param response
     */
    private void setUnauthorizedInfo(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("WWW-Authenticate", "not-login");
        response.setHeader("Cache-Control", "no-store");
    }
}
