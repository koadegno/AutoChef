

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
    //"C:\\Users\\Django\\Desktop\\ULB\\git\\2022-groupe01\\team\\ingredients\\ingredients.xlsx"
    String excelFilePath ="C:\\Users\\Django\\Desktop\\ingredient.xlsx" ;

    public static void main(String[] args) {
        ChargeDatabase chargeDatabase = new ChargeDatabase();
        chargeDatabase.parserExcelFile();
    }

    private void parserExcelFile() {

        try {
            long start = System.currentTimeMillis();
            // chargement du fichier
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);

            //iterateur pour le sheet
            Iterator<Row> rowIterator = firstSheet.iterator();

            int count = 0;

            ((Iterator<?>) rowIterator).next(); // pas prendre en compte les headers

            String ingredientName = "";
            String ingredientType = "";
            String ingredientUnit = "";
            String insertFamilleAliment = "INSERT INTO FamilleAliment(Nom) VALUES (";
            String insertIngredient = "INSERT INTO Ingredient(Nom, FamilleAlimentID) VALUES (";
            String insertIngredientSecondPart = "(SELECT FamilleAlimentID from FamilleAliment WHERE FamilleAliment.Nom = ";

            //
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 3 -> { //1er colonne
                            ingredientType = nextCell.getStringCellValue();
                        }
                        case 6 -> { //2eme colonne
                            ingredientName = nextCell.getStringCellValue();

                        }
                        default -> {
                        }
                    }
                    //replace string
                    String queryFamilleAliment = insertFamilleAliment + "'" +ingredientType+"'" + ")";
                    String queryIngredient = insertIngredient + "'" +ingredientName+"'" + "," +insertIngredientSecondPart +"'" + ingredientType+"'"  + "))";
                    //if(db.sendRequest(queryIngredient)){
                    if(db.sendRequest(queryIngredient)){
                        System.out.println("OK POUR 2 : "+queryIngredient );
                    }
                    else{
                        System.out.println("PAS BON : "+queryIngredient );

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
