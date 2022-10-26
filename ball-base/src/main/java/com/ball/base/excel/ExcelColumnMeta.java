package com.ball.base.excel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;

/**
 * @author JimChery
 */
@Setter
@Getter
@Accessors(chain = true)
public class ExcelColumnMeta {
    /**
     * 表头
     */
    private String title;

    /**
     * 行高
     */
    private short height;

    /**
     * 列宽
     */
    private int width;

    /**
     * 日期格式化
     */
    private DateTimeFormatter formatter;

    private Map<String, String> transfer;

    public String getDateString(TemporalAccessor accessor) {
        if (formatter != null) {
            return formatter.format(accessor);
        }
        return accessor.toString();
    }

    public String getValue(Object obj) {
        String value = obj.toString();
        if (transfer != null) {
            String temp = transfer.get(value);
            if (temp != null) {
                value = temp;
            }
        }
        return value;
    }
}
