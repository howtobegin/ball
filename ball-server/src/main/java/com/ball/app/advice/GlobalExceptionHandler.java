package com.ball.app.advice;


import com.ball.base.advice.BaseGlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(value = "com.ball.app.controller")
@ResponseBody
@Slf4j
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {

}
