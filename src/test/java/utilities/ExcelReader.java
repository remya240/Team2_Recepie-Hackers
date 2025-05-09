package utilities;


import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    //  String filePath;
    Workbook workbook;
    static String filePath;

    public ExcelReader(String filePath) {

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fileInputStream);
        } catch (IOException | EncryptedDocumentException e) {
            e.printStackTrace();
        }
    }

    public List<String> readColumnFromExcel(int columnIndex, int sheetno) {
        List<String> columnData = new ArrayList<>();

        try {
            Sheet sheet = workbook.getSheetAt(sheetno);
            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
                    Cell cell = row.getCell(columnIndex);


                    if (cell != null) {
                        columnData.add(cell.getStringCellValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return columnData;
    }

    public void closeWorkbook() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}