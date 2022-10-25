package com.ball.biz.socket;

import com.ball.base.util.Base64Util;
import com.ball.biz.user.service.IUserLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author littlehow
 */
@Slf4j
public class WebsocketManager {
    private static final String TOKEN_NAME = "ball:app:token:sessions:";
    private static final Map<Long, Session> sessionPool = new ConcurrentHashMap<>();
    private static final Map<Long, String> userToken = new HashMap<>();
    private static AtomicInteger online = new AtomicInteger(0);
    private static RedisTemplate<String, Object> redisTemplate;
    private static IUserLoginLogService userLoginLogService;

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        WebsocketManager.redisTemplate = redisTemplate;
    }

    public static void setUserLoginLogService(IUserLoginLogService userLoginLogService) {
        WebsocketManager.userLoginLogService = userLoginLogService;
    }

    public static void onOpen(Session session, String token){
        // 从redis里面获取用户信息
        Long uid = getUid(token, true);
        if (uid <= 0L) {
            log.warn("非法的token接入请求:{}", token);
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭session链接失败:{}", token, e);
            }
            return;
        }
        userToken.put(uid, Base64Util.decode2String(token));
        Session old = sessionPool.put(uid, session);
        if (old != null) {
            log.info("session kick account {}", uid);
            online.decrementAndGet();
            try {
                old.close();
            } catch (IOException e) {
                log.error("close session fail");
            }
        }
        log.info("current online session count {}, new account {}", online.incrementAndGet(), uid);
    }

    public static void onClose(String token){
        Long uid = getUid(token, false);
        if (uid > 0) {
            String old = userToken.get(uid);
            if (Base64Util.decode2String(token).equals(old)) {
                sessionPool.remove(uid);
                userToken.remove(uid);
                log.info("current online session count {}, remove uid {}", online.decrementAndGet(), uid);
            }
        }
    }

    public static void onMessage(String message, Session session) {
        log.info("receive message {}", message);
        session.getAsyncRemote().sendText("welcome to websocket");
    }

    public static void onError(Session session, Throwable error) {
        log.error("websocket error {}", session.getId(), error);
    }


    public static void sendMessage(Long uid, String message) {
        Session session = sessionPool.get(uid);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessage(Long uid, String token, String message) {
        try {
            Session session = sessionPool.get(uid);
            if (session != null) {
                String old = userToken.get(uid);
                if (token.equals(old)) {
                    session.getAsyncRemote().sendText(message);
                }
            }
        } catch (Exception e) {
            log.error("发送消息失败:{}", message, e);
        }

    }

    public static void sendMessage(String message) {
        // 全员发信息
        sessionPool.values().forEach(session -> session.getAsyncRemote().sendText(message));
    }

    private static Long getUid(String token, boolean isConn) {
        token = Base64Util.decode2String(token);
        String key = TOKEN_NAME + token;
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            // 如果是连接，则直接返回0，否则需要从数据库查询
            if (isConn) {
                return 0L;
            } else {
                return userLoginLogService.getBySessionId(token);
            }
        } else {
            String obj = (String)redisTemplate.opsForHash().get(key, "sessionAttr:user");
            try {
                if (StringUtils.hasText(obj)) {
                    return Long.valueOf(obj);
                }
            } catch (Exception e) {
                // slip
            }
        }
        return 0L;
    }
}
