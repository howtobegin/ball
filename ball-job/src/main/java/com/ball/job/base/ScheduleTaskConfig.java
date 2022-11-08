package com.ball.job.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.*;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Lazy(false)
@Configurable
@Component
public class ScheduleTaskConfig implements SchedulingConfigurer {
    private Logger log = LoggerFactory.getLogger(ScheduleTaskConfig.class);
    public final static String TASK_NOT_EXISTS = "not exists";
    public final static String TASK_TYPE_ERROR = "change task dataType must be trigger";
    public final static String EXPRESSION_ERROR = "expression error";
    private final static String TASK_EXISTS = "exists";
    private final static String FAILURE = "failure";
    private final static String SUCCESS = "success";
    private ScheduledTaskRegistrar scheduledTaskRegistrar;
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
        initTask();
    }

    /**
     * 添加任务
     * @param task
     * @return
     */
    String addTask(BaseTask task) {
        if (scheduledTaskRegistrar == null || task == null) {
            return FAILURE;
        }
        if (TaskConfig.isRun(task.getId())) {
            return TASK_EXISTS;
        }
        try {
            addTask0(task);
            TaskConfig.addTask(task);
            return SUCCESS;
        } catch (Exception e) {
            log.error("新增定时任务失败:" + task, e);
            throw e;
        }
    }

    /**
     * 改变任务执行频率
     * @param taskId
     * @param expression
     * @return
     */
    void changeTask(String taskId, String expression) {
        BaseTask baseTask = TaskConfig.getTask(taskId);
        Assert.notNull(baseTask, TASK_NOT_EXISTS);
        Assert.isTrue(TaskType.TRIGGER == baseTask.taskType, TASK_TYPE_ERROR);
        Assert.isTrue(StringUtils.hasText(expression), EXPRESSION_ERROR);
        log.info("change trigger expression:(id=" + taskId + ",expression=" + expression+")");
        baseTask.updateTime();
        baseTask.setExpression(expression);
    }

    /**
     * 取消定时任务
     * @param taskId
     * @return
     */
    String cancelTask(String taskId) {
        if (!TaskConfig.isRun(taskId)) {
            return TASK_NOT_EXISTS;
        }
        try {
            log.info("cancel task:" + taskId);
            TaskConfig.removeTask(taskId).getScheduledTask().cancel();
        } catch (Exception e) {
            log.error("取消任务失败:" + taskId, e);
            throw e;
        }
        return SUCCESS;
    }


    /**
     * 初始化已配置任务
     */
    private void initTask() {
        TaskConfig.getTasks().forEach(this::addTask0);
    }

    private void addTask0(BaseTask task) {
        log.info("add task:" + task);
        switch (task.taskType) {
            case TRIGGER: task.setScheduledTask(addTriggerTask(task));
                break;
            case CRON: task.setScheduledTask(addCronTask(task, task.getExpression()));
                break;
            case FIXED_RATE: task.setScheduledTask(addFixedRateTask(task, task.interval()));
                break;
            case FIXED_DELAY: task.setScheduledTask(addFixedDelayTask(task, task.interval(), task.delay()));
                break;
            default:
        }
    }

    /**
     * 添加不可改变时间表的定时任务
     * @param task
     */
    private ScheduledTask addCronTask(Runnable task, String expression) {
        return scheduledTaskRegistrar.scheduleCronTask(new CronTask(task, expression));
    }

    /**
     * 添加可变时间task
     * @param task
     * @return
     */
    private ScheduledTask addTriggerTask(BaseTask task) {
        return scheduledTaskRegistrar.scheduleTriggerTask(new TriggerTask(task, triggerContext -> {
            CronTrigger trigger = new CronTrigger(task.getExpression());
            return trigger.nextExecutionTime(triggerContext);
        }));
    }

    /**
     * 设置固定频率的定时任务
     * @param task
     * @param interval
     */
    private ScheduledTask addFixedRateTask(Runnable task, long interval) {
        return scheduledTaskRegistrar.scheduleFixedRateTask(new FixedRateTask(task, interval, 0L));
    }

    /**
     * 设置延迟以固定频率执行的定时任务
     * @param task
     * @param interval
     * @param delay
     */
    private ScheduledTask addFixedDelayTask(Runnable task, long interval, long delay) {
        return scheduledTaskRegistrar.scheduleFixedDelayTask(new FixedDelayTask(task, interval, delay));
    }
}
