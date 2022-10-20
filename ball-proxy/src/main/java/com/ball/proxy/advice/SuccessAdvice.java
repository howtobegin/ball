package com.ball.proxy.advice;

import com.ball.base.advice.SuccessResponseAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author littlehow
 */
@ControllerAdvice(basePackages = "com.ball.proxy.controller")
public class SuccessAdvice extends SuccessResponseAdvice {
}
