package com.ball.base.excel;

import java.lang.annotation.*;

/**
 * @author littlehow
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelColumn {
    /**
     * @return - 表头
     */
    String title();

    /**
     * @return - 顺序
     */
    int order();

    /**
     * @return 日期格式化
     */
    String format() default "";

    /**
     * 转义值  格式如 1=发送中,2=完成
     * @return -
     */
    String transfer() default "";

    /**
     * @return 行高
     */
    short height() default 450;

    /**
     * @return - 列宽
     */
    int width() default 4800;
}
