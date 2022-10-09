package com.ball.base.advice;

import com.ball.base.exception.*;
import com.ball.base.model.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;

import static com.ball.base.i18n.LocalUtils.getLocale;


/**
 * 全局异常处理器基类
 */
@Slf4j
public class BaseGlobalExceptionHandler {

    private static String PARAM_ERROR_CODE = "106";

    private static String SYSTEM_ERROR_CODE = "102";

    private static String KICK_OUT_CODE = "K101";

    private static String SYSTEM_ERROR = "System is busy, please try again later";

    @Autowired
    @Qualifier("myMessageSource")
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest request;

    /**
     * 拦截自定义的异常
     */
    @ExceptionHandler(BizErr.class)
    @ResponseStatus(value = HttpStatus.OK)
    public CommonResp handleBizException(BizErr e) {
        log.warn(e.getMessage(), e);
        return CommonResp.error(e.getCode().getCode(), getLocaleMessage(e.getCode().getCode(), e.getMessage(), e.getArgs()));
    }

    @ExceptionHandler(KickOutException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public CommonResp handleKickOutException(KickOutException exception) {
        return CommonResp.error(KICK_OUT_CODE, exception.getMessage());
    }

    /**
     * 拦截自定义的异常
     */
    @ExceptionHandler(SysErr.class)
    @ResponseStatus(value = HttpStatus.OK)
    public CommonResp handleSysException(SysErr e) {
        log.warn(e.getMessage(), e);
        return CommonResp.error(e.getCode(), e.getDesc());
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResp methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}, {}", e.getMessage(), e);

        String errorMessage = getErrMessage(e.getBindingResult());
        return CommonResp.error(PARAM_ERROR_CODE, errorMessage);
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public CommonResp methodArgumentNotValidExceptionHandler(HttpMessageNotReadableException e) {
        log.warn("MethodArgumentNotValidException: {}, {}", e.getMessage(), e);

        String errorMessage = e.getLocalizedMessage();
        return CommonResp.error(PARAM_ERROR_CODE, errorMessage);
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResp validationErrorHandler(ConstraintViolationException e) {
        log.warn("ConstraintViolationException: {}, {}", e.getMessage(), e);

        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .reduce((s1, s2) -> s1.concat(",").concat(s2)).get();
        return CommonResp.error(PARAM_ERROR_CODE, errorMessage);
    }

    /**
     * controller入参校验异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = BindException.class)
    public CommonResp bindExceptionHandler(BindException e) {
        log.warn("BindException: {}, {}", e.getMessage(), e);

        String errorMessage = getErrMessage(e.getBindingResult());
        return CommonResp.error(PARAM_ERROR_CODE, errorMessage);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public CommonResp bindArgumentException(IllegalArgumentException e) {
        return CommonResp.error(PARAM_ERROR_CODE, e.getMessage());
    }

    /**
     * 全局异常捕获       需要返回500
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Object exceptionHandler(HttpServletRequest request, Throwable e) {
        log.error("catch unexpected exception happened, url: {}, exception message : {}", request.getRequestURI(), e.getMessage(), e);
        return CommonResp.error(SYSTEM_ERROR_CODE, SYSTEM_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void notFoundExceptionHandler(FileNotFoundException e) {
        log.warn("BindException: {}, {}", e.getMessage(), e);
    }

    @ExceptionHandler(AssertException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public CommonResp assertExceptionHandler(AssertException e) {
        log.warn("AssertException: {}, {}", e.getMessage(), e);
        IBizErrCode errCode = e.getCode();
        return CommonResp.error(errCode.getCode(), getLocaleMessage(errCode.getCode(), errCode.getDesc(), e.getArgs()));
    }


    /**
     * 获取国际化异常信息(支持url中包含lang和浏览器的Accept-Language)
     */
    protected String getLocaleMessage(String code, String defaultMsg, Object[] params) {
        try {
            return messageSource.getMessage(code, params, getLocale(request));
        } catch (Exception e) {
            log.warn("本地化异常消息发生异常: {}, {}", code, params);
            try {
                return MessageFormat.format(defaultMsg, params);
            } catch (Exception e1) {
                return defaultMsg;
            }
        }
    }

    private static String getErrMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .reduce((s1, s2) -> s1.concat(",").concat(s2)).get();
    }
}
