package com.ball.job.base;

import com.ball.base.util.TraceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author littlehow
 */
public abstract class BaseBizTask extends BaseTriggerTask {
    @Autowired
    @Qualifier("threadPoolExecutor")
    protected ThreadPoolExecutor executor;

    protected void submit(Runnable runnable) {
        executor.submit(() -> {
            try {
                TraceUtil.start();
                runnable.run();
            } finally {
                TraceUtil.end();
            }
        });
    }
}
