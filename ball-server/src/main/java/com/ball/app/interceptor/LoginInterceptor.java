package com.ball.app.interceptor;


import com.ball.base.context.AppLoginUser;
import com.ball.base.context.UserContext;
import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    public static final String SESSION_USER = "user";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String userId = (String) session.getAttribute(SESSION_USER);
        if (StringUtils.isEmpty(userId)) {
            setUnauthorizedInfo(httpServletResponse);
            return false;
        }

        Long userNo = Long.valueOf(userId);
        // todo littlehow add user info
//        UserInfo userInfo = userService.getByUserId(userNo);
//        BizAssert.isTrue(UserStatus.NORMAL.isMe(userInfo.getStatus()), BizErrCode.USER_LOCKED);
//        UserContext.set(new AppLoginUser().setUserNo(userNo));
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
