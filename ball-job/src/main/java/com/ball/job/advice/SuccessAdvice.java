package com.ball.job.advice;

import com.ball.base.advice.SuccessResponseAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author JimChery
 */
@ControllerAdvice(basePackages = "com.ball.job.controller")
public class SuccessAdvice extends SuccessResponseAdvice {
}
