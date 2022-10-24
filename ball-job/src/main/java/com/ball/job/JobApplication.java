package com.ball.job;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ball"})
@EnableScheduling
@EnableSwagger2Doc
@EnableKnife4j
@PropertySource("classpath:swagger.properties")
@Slf4j
public class JobApplication {
    private final AtomicInteger scheduledId = new AtomicInteger(1);
    private final AtomicInteger executorId = new AtomicInteger(1);

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
        log.info("nft job application running...");
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(8, r ->
                new Thread(r, "ball-schedule-thread-" + scheduledId.getAndIncrement())
        );
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(10, 100, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), r -> new Thread(r, "ball-execute-thread-" + executorId.getAndIncrement()));
    }
}
