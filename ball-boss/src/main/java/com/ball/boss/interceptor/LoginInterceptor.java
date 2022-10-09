package com.ball.boss.interceptor;

import com.ball.base.util.Base64Util;
import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import com.ball.boss.context.BossUserContext;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.service.system.LoginService;
import com.ball.boss.service.system.UserService;
import com.ball.boss.service.system.model.LockEnum;
import com.ball.boss.service.system.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.ball.boss.config.HttpSessionConfig.TOKEN_NAME;

public class LoginInterceptor implements HandlerInterceptor {

    public static final String SESSION_USER = "user";

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Value("${boss.check.google:false}")
    private boolean checkGoogle;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        HttpSession session = httpServletRequest.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(SESSION_USER);
        if (userInfo == null) {
            String token = getToken(httpServletRequest);
            // 判断有无token
            if (StringUtils.hasText(token)) {
                loginService.checkKickOut(Base64Util.decode2String(token));
            }
            setUnauthorizedInfo(httpServletResponse);
            return false;
        }
        BossUserInfo userInfoPo = userService.queryById(userInfo.getUserId());
        if (!LockEnum.isNotLock(userInfoPo.getLocked())) {
            setUnauthorizedInfo(httpServletResponse);
            return false;
        }
        checkGoogle(httpServletRequest, userInfoPo);
        BossUserContext.set(userInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //删除设置的上下文
        BossUserContext.remove();
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

    private void checkGoogle(HttpServletRequest request, BossUserInfo userInfo) {
        if (checkGoogle && !StringUtils.hasText(userInfo.getGoogleKey())) {
            // 只有绑定google相关请求可以放过
            String uri = request.getRequestURI();
            BizAssert.isTrue(uri.equalsIgnoreCase("/boss/system/user/getGoogleSecret")
                    || uri.equalsIgnoreCase("/boss/system/user/bindGoogle")
                    || uri.equalsIgnoreCase("/boss/account/logout"), BizErrCode.NEED_BIND_GOOGLE_CODE);
        }
    }
}
