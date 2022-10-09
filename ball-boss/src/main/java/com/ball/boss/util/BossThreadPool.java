package com.ball.boss.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author littlehow
 */
public class BossThreadPool {
    private static final AtomicInteger ID = new AtomicInteger(1);
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 1, 0,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000000),
            r -> new Thread(r, "boss-executor-" + ID.getAndIncrement()));

    public static void execute(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public static void setCoreSize(int coreSize) {
        int oldSize = EXECUTOR.getPoolSize();
        if (oldSize <= coreSize) {
            // 阻止核心线程数缩小
            return;
        }
        int maxSize = EXECUTOR.getMaximumPoolSize();
        if (maxSize < coreSize) {
            EXECUTOR.setMaximumPoolSize(coreSize);
        }
        EXECUTOR.setCorePoolSize(coreSize);
    }
}
