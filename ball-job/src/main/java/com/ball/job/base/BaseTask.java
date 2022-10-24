package com.ball.job.base;


import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基本的任务配置类
 */
@Slf4j
public abstract class BaseTask implements Runnable, InitializingBean {
    protected boolean enabled;
    public final TaskType taskType;
    private ScheduledTask scheduledTask;
    protected final String id;
    protected String taskName;
    public final long createTime;
    private long updateTime;
    private int status;
    private AtomicBoolean running;
    @Autowired
    protected ScheduleBaseService scheduleBaseService;
    public BaseTask(TaskType taskType) {
        this.taskType = taskType;
        this.id = this.getClass().getName();
        this.taskName = this.getClass().getSimpleName();
        this.createTime = System.currentTimeMillis();
        this.updateTime = createTime;
        this.status = YesOrNo.YES.v;
        this.running = new AtomicBoolean(false);
    }

    /**
     *  获取任务表达式如：0 0 0/1 * * *? (每个整点执行)
     * @return
     */
    public abstract String getExpression();

    /**
     * 固定频率执行的时间间隔
     * @return
     */
    public abstract long interval();

    /**
     * 固定频率执行的延迟时间
     * @return
     */
    public abstract long delay();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置任务表达式
     * @param expression
     */
    public abstract void setExpression(String expression);

    public void updateTime() {
        this.updateTime = System.currentTimeMillis();
    }

    public long getUpdateTime() {
        return updateTime;
    }

    /**
     * 获取任务唯一标识
     * @return
     */
    public String getId() {
        return id;
    }

    public final ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    public final void setScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public abstract void execute();

    @Override
    public void run() {
        if (!enabled) {
            log.info("{} job not enabled", taskName);
            return;
        }
        try {
            TraceUtil.start();
            log.info("{} task start...", taskName);
            if (running.compareAndSet(false, true)) {
                execute();
                running.compareAndSet(true, false);
            }
        } catch (Exception e) {
            running.compareAndSet(true, false);
            log.error("fetch task {} error", taskName, e);
        } finally {
            log.info("{} task end...", taskName);
            TraceUtil.end();
        }
    }

    @Override
    public void afterPropertiesSet() {
        TaskConfig.addTask(this);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(id:" + id + ",expression:" + getExpression()
                + ",type:" + taskType + ",interval:" + interval()
                + ", delay:" + delay() + ")";
    }
}
