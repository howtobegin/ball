package com.ball.job.base;


import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class BaseTriggerTask extends BaseTask {
    private String expression;
    public BaseTriggerTask() {
        super(TaskType.TRIGGER);
    }

    @Override
    public void setExpression(String expression) {
        Assert.isTrue(StringUtils.hasText(expression), ScheduleTaskConfig.EXPRESSION_ERROR);
        String[] ss = expression.split(" ");
        Assert.isTrue(ss.length == 6, ScheduleTaskConfig.EXPRESSION_ERROR);
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public final long interval() {
        return 0L;
    }

    public final long delay() {
        return 0L;
    }
}
