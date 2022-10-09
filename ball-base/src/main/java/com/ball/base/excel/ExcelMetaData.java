package com.ball.base.excel;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;

/**
 * @author littlehow
 */
@Setter
@Getter
public class ExcelMetaData {
    /**
     * 字段
     */
    private Field field;

    /**
     * 对应的get方法
     */
    private Method method;

    /**
     * 排序
     */
    private int order;

    /**
     * 日期顶级接口
     */
    private boolean isTemporalAccessor = false;

    /**
     * 是否为decimal类型
     */
    private boolean isDecimal = false;

    /**
     * 字段属性
     */
    private ExcelColumnMeta columnMeta;

    /**
     * 获取对象的值，做简单类型的格式化
     * @param obj - 对象
     * @return - obj对应该字段的格式化值
     */
    public String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        Object value = getValue(obj);
        if (value == null) {
            return "";
        }

        if (isTemporalAccessor) {
            return columnMeta.getDateString((TemporalAccessor) value);
        } else if (isDecimal) {
            return ((BigDecimal) value).stripTrailingZeros().toPlainString();
        } else {
            return columnMeta.getValue(value.toString());
        }
    }

    /**
     * 获取对象的值, 优先get方法获取，没有get方法的将使用field获取，所以如果没有提供get方法，需要将字段访问属性设置为public
     * 这里就不对字段进行field.setAccessible设置
     * @param obj  - 对象
     * @return - 对应对应的值
     */
    private Object getValue(Object obj) {
        try {
            if (method != null) {
                return method.invoke(obj);
            } else {

                return field.get(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
