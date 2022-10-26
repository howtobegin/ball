package com.ball.base.excel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author JimChery
 */
public class ExcelWrite {

    public static void writeExcel(List<String> headers, List<Integer> columnWidths, List<List<String>> rows, OutputStream os) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        SXSSFSheet sheet = workbook.createSheet();
        // 设置列宽
        setColumnWidth(sheet, columnWidths);
        // 创建表头
        SXSSFRow header = sheet.createRow(0);
        header.setHeight((short) 450);
        XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
        setContent(header, headers, style);
        // 设置内容
        setRows(sheet, rows, workbook);
        // 输出
        workbook.write(os);
    }

    static void setColumnWidth(SXSSFSheet sheet, List<Integer> columnWidths) {
        for (int i = 0, len = columnWidths.size(); i < len; i++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }
    }

    static void setContent(SXSSFRow row, List<String> content, XSSFCellStyle style) {
        // 设置第一行
        for (int i = 0, len = content.size(); i < len; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellValue(content.get(i));
            // 创建一个单元格样式
            cell.setCellStyle(style);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        }
    }

    static void setRows(SXSSFSheet sheet, List<List<String>> rows, SXSSFWorkbook workbook) {
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }

        XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
        for (int i = 0, size = rows.size(); i < size; i ++) {
            // 创建表头
            SXSSFRow row = sheet.createRow(i + 1);
            row.setHeight((short) 450);
            setContent(row, rows.get(i), style);
        }
    }

}
