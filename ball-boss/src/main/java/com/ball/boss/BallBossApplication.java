package com.ball.boss;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author JimChery
 */
@SpringBootApplication
@EnableSwagger2Doc
@EnableKnife4j
@EnableAsync
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackages = {"com.ball"})
@Slf4j
public class BallBossApplication {
    public static void main(String[] args) {
        SpringApplication.run(BallBossApplication.class, args);
        log.info("ball boss running...");
    }
}
