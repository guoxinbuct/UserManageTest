package com.test.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtil<main> {

    public static Sheet getSheet(String filePath,int sheetIndex) throws IOException {
        Sheet sheet=null;
        Workbook wb=null;
        InputStream is=new FileInputStream(filePath);
        if(filePath.endsWith("xlsx")){
            wb=new XSSFWorkbook(is);
        }else{
            wb=new HSSFWorkbook(is);
        }
        sheet=wb.getSheetAt(sheetIndex);
        return sheet;
    }

    public static Row getRow(String filePath,int sheetIndex,int rowIndex) throws IOException {
        Row row=null;
        Sheet sheet=getSheet(filePath,sheetIndex);
        row=sheet.getRow(rowIndex);
        return row;
    }

    public static Cell getCell(String filePath, int sheetIndex, int rowIndex, int cellIndex) throws IOException {
        Cell cell=null;
        Row row=getRow(filePath,sheetIndex,rowIndex);
        cell=row.getCell(cellIndex);
        return cell;
    }

    public static Object[][] getExcelValues(String filePath,int sheetIndex) throws IOException {
        Sheet sheet=getSheet(filePath,sheetIndex);
        int totalRowNum=sheet.getLastRowNum();
        int totalCellNum=sheet.getRow(0).getLastCellNum();
        Object[][] values=new Object[totalRowNum][totalCellNum];
        System.out.println("totalCellNum="+totalCellNum+";totalRowNum="+totalRowNum);
        for(int rowNum=1;rowNum<=totalRowNum;rowNum++){
            for(int cellNum=0;cellNum<totalCellNum;cellNum++){
                Cell cell=sheet.getRow(rowNum).getCell(cellNum);
                values[rowNum-1][cellNum]=getValueByCellType(cell);
            }
        }
        return values;
    }

    public static Object getValueByCellType(Cell cell){
        Object object=null;
        switch (cell.getCellType()){
            case _NONE:
                object="";break;
            case BLANK:
                object="";break;
            case ERROR:
                object="";break;
            case STRING:
                object=cell.getStringCellValue().trim();break;
            case BOOLEAN:
                object=cell.getBooleanCellValue();break;
            case FORMULA:
                object=cell.getCellFormula();break;
            case NUMERIC:
                object=cell.getNumericCellValue();break;
            default:
                object=cell.getDateCellValue();break;
        }
        return object;

    }

    public static void main(String[] args) throws IOException {
        String filePath="/users/guoxin/test/test.xlsx";
        Cell cell=getCell(filePath,0,1,4);
        //System.out.println(cell.getDateCellValue().toString());
        System.out.println(cell.getCellType());
        Object[][] values=getExcelValues(filePath,0);
        System.out.println("二维数组里面的内容如下*****************************");
        for(int i=0;i<values.length;i++){
            for(int j=0;j<values[i].length;j++){
                System.out.print(values[i][j]+"\t");
            }
            System.out.println();
        }
    }
}
