package f.repositories;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import f.models.Card;

public class ExcelMapper {

    XSSFWorkbook workbook;
    XSSFSheet sheet;

    public ExcelMapper(File file) throws IOException {
        workbook = new XSSFWorkbook(new FileInputStream(file));
        sheet = workbook.getSheetAt(0);
    }

    public List<Card> getCardsAsList(){
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<Card> list = new ArrayList<>();
        if(rowIterator.hasNext()) {
            rowIterator.next();
        }
        while(rowIterator.hasNext()){
            String question;
            String answer;
            Integer period;
            Date date;
            Integer pack;

            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;
            CellType type;
            cell = cellIterator.next();

            question = cell.getStringCellValue();
            cell = cellIterator.next();

            answer = cell.getStringCellValue();
            cell = cellIterator.next();

            period = (int)(cell.getNumericCellValue());
            cell = cellIterator.next();

            date = cell.getDateCellValue();
            cell = cellIterator.next();

            pack = (int)cell.getNumericCellValue();

            list.add(new Card(null, question, answer, done, pack));
        }
        return list;
    }


}

