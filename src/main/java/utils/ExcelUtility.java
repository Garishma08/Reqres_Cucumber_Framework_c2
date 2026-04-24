package utils;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;

public class ExcelUtility {

    public static String getCellData(String sheetName, int rowNum, String columnName) {

        try {
            FileInputStream fis = new FileInputStream(ConfigReader.getExcelPath());
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            Row headerRow = sheet.getRow(0);
            int colIndex = -1;

            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                    colIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (colIndex == -1) {
                throw new RuntimeException("Column not found: " + columnName);
            }

            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(colIndex);

            DataFormatter formatter = new DataFormatter();
            String value = formatter.formatCellValue(cell);

            workbook.close();
            fis.close();

            return value;

        } catch (Exception e) {
            throw new RuntimeException("Excel read failed: " + e.getMessage());
        }
    }
}