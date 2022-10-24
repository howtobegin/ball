package com.ball.job.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author littlehow
 */
@Setter
@Getter
public class UpdateJobConfigVO extends JobBaseVO {

    @ApiModelProperty(value = "cron表达式", required = true)
    @NotBlank(message = "expression must be not null")
    private String expression;
}
