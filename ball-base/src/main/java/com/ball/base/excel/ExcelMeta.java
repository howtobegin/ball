package com.ball.base.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 记录表宽度、表头、行高以及excel字段信息
 * @author JimChery
 */
@Setter
@Getter
public class ExcelMeta {
    /**
     * 列宽
     */
    private List<Integer> width;

    /**
     * 行高
     */
    private short height;

    /**
     * 表头
     */
    private List<String> header;

    /**
     * 字段属性集
     */
    private List<ExcelMetaData> excelMetaData;
}
