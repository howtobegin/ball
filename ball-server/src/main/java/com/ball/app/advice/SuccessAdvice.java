package com.ball.app.advice;

import com.ball.base.advice.SuccessResponseAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author JimChery
 */
@ControllerAdvice(basePackages = "com.ball.app.controller")
public class SuccessAdvice extends SuccessResponseAdvice {
}
