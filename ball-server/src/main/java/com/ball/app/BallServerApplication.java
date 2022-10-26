package com.ball.app;

import com.ball.app.controller.socket.WebSocketServer;
import com.ball.biz.user.service.IUserLoginLogService;
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
public class BallServerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BallServerApplication.class, args);
        // 对websocket进行必要元素的注入
        WebSocketServer.setRedisTemplate((RedisTemplate<String, Object>)context.getBean("redisTemplate"));
        WebSocketServer.setUserLoginLogService((IUserLoginLogService) context.getBean("userLoginLogServiceImpl"));
        log.info("ball server application running...");
    }

}
