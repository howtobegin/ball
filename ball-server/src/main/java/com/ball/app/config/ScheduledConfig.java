package com.ball.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author JimChery
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {

    /**
     * 任务执行线程池大小
     */
    private static final int TASK_POOL_SIZE = 8;
    /**
     * 线程名
     */
    private static final String TASK_THREAD_PREFIX = "ball-schedule-thread-";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler taskPool = new ThreadPoolTaskScheduler();
        taskPool.setPoolSize(TASK_POOL_SIZE);
        taskPool.setThreadNamePrefix(TASK_THREAD_PREFIX);
        taskPool.initialize();
        scheduledTaskRegistrar.setTaskScheduler(taskPool);
    }
}
