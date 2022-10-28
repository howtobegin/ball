package com.ball.biz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lhl
 * @date 2022/10/27 下午6:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    String key();

    String subKey() default "";

    /**
     * 单位毫秒
     * @return
     */
    int time();

    /**
     * 是否对每个用户限制，true ? userNo + subKey : subKey
     * @return
     */
    boolean forUser() default true;

    String msg() default "so fast";
}
