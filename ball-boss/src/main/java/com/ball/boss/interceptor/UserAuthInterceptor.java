package com.ball.boss.interceptor;

import com.ball.boss.context.BossUserContext;
import com.ball.boss.service.system.UserService;
import com.ball.boss.service.system.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        UserInfo currentUser = BossUserContext.get();

        // 权限检测
        boolean authorized = true; // userService.checkPermission(currentUser.getUserId(), httpServletRequest.getRequestURI());

        // 没有权限
        if (!authorized) {
            log.warn("用户[{}]正在尝试请求没有权限的资源[{}]", currentUser.getUserId(), httpServletRequest.getRequestURI());

            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setHeader("Cache-Control", "no-store");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) {
        // to do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
        // to do nothing
    }

}
