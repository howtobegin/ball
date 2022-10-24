package com.ball.job.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author littlehow
 */
@Getter
@Setter
public class JobBaseVO {
    @ApiModelProperty(value = "任务类", required = true)
    @NotBlank(message = "job class must be not null")
    private String jobClass;
}
