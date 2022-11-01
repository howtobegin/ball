package com.ball.proxy;

import com.ball.biz.user.service.IUserLoginLogService;
import com.ball.proxy.controller.socket.WebSocketServer;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author JimChery
 */
@SpringBootApplication
@EnableSwagger2Doc
@EnableKnife4j
@EnableAsync
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackages = {"com.ball"})
@EnableScheduling
@Slf4j
public class BallProxyApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BallProxyApplication.class, args);
        // 对websocket进行必要元素的注入
        WebSocketServer.setRedisTemplate((RedisTemplate<String, Object>)context.getBean("redisTemplate"));
        WebSocketServer.setUserLoginLogService((IUserLoginLogService) context.getBean("userLoginLogServiceImpl"));
        log.info("ball proxy application running...");
    }
}
