package com.ball.job.controller.vo;

import com.ball.job.base.BaseTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JimChery
 */
@Setter
@Getter
public class JobConfigVO {
    @ApiModelProperty("任务类")
    private String jobClass;

    @ApiModelProperty("任务类型：TRIGGER-可改变表达式 CRON:不可改变表达式 FIXED_RATE:定期 FIXED_DELAY:延迟定期")
    private String jobType;

    @ApiModelProperty("任务移除状态 1:正常 0:取消")
    private Integer status;

    @ApiModelProperty("业务执行状态 true:执行 false:不执行")
    private Boolean enabled;

    @ApiModelProperty("表达式 TRIGGER和CRON有该值")
    private String expression;

    @ApiModelProperty("定期执行毫秒数 FIXED_RATE和FIXED_DELAY关注")
    private Long interval;

    @ApiModelProperty("延迟毫秒数 FIXED_DELAY关注")
    private Long delay;

    @ApiModelProperty("本次进入系统时间")
    private Long createTime;

    @ApiModelProperty("最后更新时间")
    private Long updateTime;

    public JobConfigVO(BaseTask task) {
        this.jobClass = task.getId();
        this.jobType = task.taskType.name();
        this.expression = task.getExpression();
        this.interval = task.interval();
        this.delay = task.delay();
        this.createTime = task.createTime;
        this.updateTime = task.getUpdateTime();
        this.status = task.getStatus();
        this.enabled = task.isEnabled();
    }
}
