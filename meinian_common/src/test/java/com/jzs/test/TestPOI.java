package com.jzs.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/27  21:09
 */
public class TestPOI {
    @Test
    public void readExcel() throws IOException {
        //创建工作簿
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook("F:/test/hello.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheet) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获得单元格中的值
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String stringCellValue = cell.getStringCellValue();//注意：数字类型,需要修改excel单元格的类型，否则报错。
                System.out.println(stringCellValue);
            }
        }
    }

    @Test
    public void importExcel() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = xssfWorkbook.createSheet("hellotest");

        //创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格，0表示第一个单元格
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("性别");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("金正森");
        row2.createCell(1).setCellValue("女");
        row2.createCell(2).setCellValue("22");

        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("梁修蝶");
        row3.createCell(1).setCellValue("女");
        row3.createCell(2).setCellValue("22");
        //通过输出流将workbook对象下载到磁盘
        FileOutputStream fileOutputStream = new FileOutputStream("f:/test/hello1.xlsx");
        xssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();
        xssfWorkbook.close();
    }

}
