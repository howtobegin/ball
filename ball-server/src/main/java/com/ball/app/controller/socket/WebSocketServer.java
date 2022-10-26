package com.ball.app.controller.socket;

import com.ball.biz.socket.WebsocketManager;
import com.ball.biz.user.service.IUserLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * 多机部署中涉及到的全局参数模型可以加上redis
 * 单机部署可直接使用
 * @author littlehow
 */
@Slf4j
@ServerEndpoint("/websocket/{token}")
@Component
public class WebSocketServer {

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        WebsocketManager.setRedisTemplate(redisTemplate);
    }

    public static void setUserLoginLogService(IUserLoginLogService userLoginLogService) {
        WebsocketManager.setUserLoginLogService(userLoginLogService);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token){
        WebsocketManager.onOpen(session, token);
    }

    @OnClose
    public void onClose(@PathParam(value = "token") String token){
        WebsocketManager.onClose(token);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        WebsocketManager.onMessage(message, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket error {}", session.getId(), error);
    }
}
