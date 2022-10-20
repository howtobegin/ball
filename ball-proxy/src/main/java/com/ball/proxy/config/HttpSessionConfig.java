package com.ball.proxy.config;

import com.ball.base.util.Base64Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7 * 86400, redisNamespace = "ball:proxy:token")
public class HttpSessionConfig {
    public static final String TOKEN_NAME = "BP-Token";

    /**
     * 将sessionId装换成base64编码
     *
     * @param request -
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        return Base64Util.encode(request.getSession().getId());
    }

    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        return new MyHeaderHttpSessionStrategy();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    /**
     * 由于 aws 默认未启用 notify-keyspace-events ，所以替换调默认的
     * {@link org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction}
     * <p>
     * https://github.com/spring-projects/spring-session/issues/124
     */
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    /**
     * 主要为了重写getRequestedSessionId方法
     */
    static class MyHeaderHttpSessionStrategy extends HeaderHttpSessionIdResolver {
        MyHeaderHttpSessionStrategy() {
            super(TOKEN_NAME);
        }

        @Override
        public List<String> resolveSessionIds(HttpServletRequest request) {
            String headerValue = request.getHeader(TOKEN_NAME);
            if (StringUtils.isBlank(headerValue)) {
                headerValue = cookieToken(request);
            }
            if (StringUtils.isNotBlank(headerValue)) {
                headerValue = Base64Util.decode2String(headerValue);
            }
            return (headerValue != null) ? Collections.singletonList(headerValue) : Collections.emptyList();
        }

        @Override
        public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
            response.setHeader(TOKEN_NAME, Base64Util.encode(sessionId));
        }
    }

    private static String cookieToken(HttpServletRequest request) {
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
}
