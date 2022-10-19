package com.ball.app.controller.socket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多机部署中涉及到的全局参数模型可以加上redis
 * 单机部署可直接使用
 * @author littlehow
 */
@Slf4j
//@ServerEndpoint("/websocket/{account}")
//@Component
public class WebSocketServer {

    private static final Map<String, Session> sessionPool = new ConcurrentHashMap<>();
    // 基于session id本身的连接管理，暂时无用
    private static final Map<String, Session> sessionIdPool = new ConcurrentHashMap<>();

    private static AtomicInteger online = new AtomicInteger(0);

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "account") String account){
        Session old = sessionPool.put(account, session);
        if (old != null) {
            log.info("session kick account {}", account);
            sessionIdPool.remove(old.getId());
            online.decrementAndGet();
            try {
                old.close();
            } catch (IOException e) {
                log.error("close session fail");
            }
        }
        sessionIdPool.put(session.getId(), session);
        log.info("current online session count {}, new account {}", online.incrementAndGet(), account);
    }

    @OnClose
    public void onClose(@PathParam(value = "account") String account){
        sessionPool.remove(account);
        log.info("current online session count {}, remove account {}", online.decrementAndGet(), account);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("receive message {}", message);
        session.getAsyncRemote().sendText("welcome to websocket");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket error {}", session.getId(), error);
    }


    public static void sendMessage(String account, String message) {
        Session session = sessionPool.get(account);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessage(String message) {
        // 全员发信息
        sessionPool.values().forEach(session -> session.getAsyncRemote().sendText(message));
    }
}
