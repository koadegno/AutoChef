

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ChargeDatabase {

    Database db = new Database("autochef.sqlite");
    String excelFilePath = "C:\\Users\\Django\\Desktop\\ULB\\git\\2022-groupe01\\team\\ingredients\\ingredients.xlsx";

    public static void main(String[] args) {
        ChargeDatabase chargeDatabase = new ChargeDatabase();
        chargeDatabase.parserExcelFile();
    }

    private void parserExcelFile() {

        try {
            long start = System.currentTimeMillis();

            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = firstSheet.iterator();

            int count = 0;

            ((Iterator<?>) rowIterator).next(); // skip the header row

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();

                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0 -> {
                            String name = nextCell.getStringCellValue();
                            System.out.print(name + " : ");
                        }
                        case 1 -> {
                            String v = nextCell.getStringCellValue();
                            System.out.println(v);
                        }
                        default -> {
                        }
                    }

                }

            }

            // execute the remaining queries

            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        } catch (Exception e){
            System.out.println(e);
        }

    }

}
