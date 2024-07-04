package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.DatabaseConnection;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    private DatabaseConnection database;

    public ExcelReader(DatabaseConnection database) {
        this.database = database;
    }

    public void readAndInsertExcelData(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String[] data = new String[row.getLastCellNum()];
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                data[i] = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                // Check if it is a numeric cell that can be converted to string
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    data[i] = cell.getDateCellValue().toString();
                                } else {
                                    data[i] = String.valueOf(cell.getNumericCellValue());
                                }
                                break;
                            case BOOLEAN:
                                data[i] = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                data[i] = cell.getCellFormula();
                                break;
                            default:
                                data[i] = ""; // Handle other types gracefully
                                break;
                        }
                    } else {
                        data[i] = ""; // Handle null cells gracefully
                    }
                    System.out.println("Extracted data: " + data[i]);
                }
                database.insertDataFromExcel(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DatabaseConnection database = new DatabaseConnection();
        ExcelReader reader = new ExcelReader(database);
        reader.readAndInsertExcelData("C:/Users/toves/Documents/Book1.xlsx");
    }
}
