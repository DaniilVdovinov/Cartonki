package f.repositories;

import android.util.Log;
import android.widget.Toast;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import f.models.Card;

import static android.content.ContentValues.TAG;

public class ExcelMapper {

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Long id;

    public ExcelMapper(File file, Long id) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(0);
        this.id = id;
    }

    public ExcelMapper(String filePath) {
        try {
            workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<Card> getCardsAsList() {
//        Iterator<Row> rowIterator = sheet.rowIterator();
        List<Card> list = new ArrayList<>();
//        if(rowIterator.hasNext()) {
//            rowIterator.next();
//        }
//        while(rowIterator.hasNext()){
//            String question;
//            String answer;
//
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.cellIterator();
//            Cell cell;
//            CellType type;
//            cell = cellIterator.next();
//
//            question = cell.getStringCellValue();
//            cell = cellIterator.next();
//
//            answer = cell.getStringCellValue();
//            cell = cellIterator.next();
//
//            list.add(new Card(null, question, answer, 0, id));
//        }
        int rowsCount = sheet.getPhysicalNumberOfRows();
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        StringBuilder sb = new StringBuilder();

        //outter loop, loops through rows
        for (int r = 0; r < rowsCount; r++) {
            Row row = sheet.getRow(r);

            String question = getCellAsString(row, 0, formulaEvaluator);
            String cellInfo = "r:" + r + "; c:" + 0 + "; v:" + question;
            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
            sb.append(question + ", ");

            String answer = getCellAsString(row, 1, formulaEvaluator);
            cellInfo = "r:" + r + "; c:" + 1 + "; v:" + answer;
            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
            sb.append(answer + ", ");

            list.add(new Card(null, question, answer, 0, id));
        }
        return list;
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }
}

