package com.ball.boss.advice;

import com.ball.base.advice.SuccessResponseAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author JimChery
 */
@ControllerAdvice(basePackages = {"com.ball.boss.controller"})
public class SuccessAdvice extends SuccessResponseAdvice {

}
