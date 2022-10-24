package com.ball.job.base;

import com.ball.base.model.enums.YesOrNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleBaseService {
    @Autowired
    private ScheduleTaskConfig config;

    /**
     * 修改任务执行频率等
     * @param taskId
     * @param expression
     * @return
     */
    public void changeTask(String taskId, String expression) {
        config.changeTask(taskId, expression);
    }

    /**
     * 禁用任务
     * @param taskId - 任务编号
     * @return -
     */
    public String disableTask(String taskId) {
        return config.cancelTask(taskId);
    }

    /**
     * 启用任务
     * @param taskId -
     */
    public void enableTask(String taskId) {
        BaseTask task = TaskConfig.getTask(taskId);
        if (task != null && YesOrNo.NO.isMe(task.getStatus())) {
            config.addTask(task);
            task.setStatus(YesOrNo.YES.v);
            task.updateTime();
        }
    }

    /**
     * 执行任务
     * @param taskId -
     */
    public void runTask(String taskId) {
        BaseTask task = TaskConfig.getTask(taskId);
        task.run();
    }
}
