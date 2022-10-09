package com.ball.boss.interceptor;

import com.ball.base.context.RequestContext;
import com.ball.base.context.RequestInfo;
import com.ball.base.util.BizAssert;
import com.ball.base.util.IpUtil;
import com.ball.base.util.TraceUtil;
import com.ball.base.util.UserPerUtil;
import com.ball.biz.exception.BizErrCode;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestInterceptor implements HandlerInterceptor {
    private static final String HEADER_TERMINAL = "terminal";
    private static final Pattern DOMAIN = Pattern.compile("(http(s)?://)?([\\w-.]+(:\\d+)?)(/)?");


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        BizAssert.isTrue(UserPerUtil.getService(), BizErrCode.SERVICE_TIMEOUT);
        TraceUtil.start();
        RequestInfo requestInfo = RequestInfo.builder()
                .ip(IpUtil.realIP(httpServletRequest))
                .uri(httpServletRequest.getRequestURI())
                .terminal(httpServletRequest.getHeader(HEADER_TERMINAL))
                .referer(getReferer(httpServletRequest))
                .build();
        RequestContext.set(requestInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        // to do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //删除设置的上下文
        RequestContext.remove();
        TraceUtil.end();
    }

    private String getReferer(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (StringUtils.hasText(referer)) {
            Matcher matcher = DOMAIN.matcher(referer);
            if (matcher.find()) {
                return matcher.group(3);
            }
        }
        return null;
    }
}
