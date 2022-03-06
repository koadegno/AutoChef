package ulb.infof307.g01.db;

import javafx.scene.text.Text;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;

public class ChargeDatabase {

    Database db = new Database("autochef.sqlite");
    //"C:\\Users\\Django\\Desktop\\ULB\\git\\2022-groupe01\\team\\ingredients\\ingredients.xlsx"
    String excelFilePath ="C:\\Users\\salma\\IdeaProjects\\2022-groupe01\\team\\recettes\\Recettes.xlsx" ;

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

            String recetteName = "";
            String categorie = "";
            String type ="";
            double nbPersonne = 0;
            double  duree = 0;
            String preparation = "";

            String updateIngredientPart1 = "UPDATE Ingredient SET UniteID = (SELECT Unite.UniteID from Unite where Unite.Nom = ";
            String updateIngredientPart2 = "where Ingredient.FamilleAlimentID is ( Select DISTINCT FamilleAliment.FamilleAlimentID from Ingredient INNER JOIN FamilleAliment On Ingredient.FamilleAlimentID = FamilleAliment.FamilleAlimentID  where FamilleAliment.Nom = ";
            String updateIngredientPart3 = "LIMIT 1)";


            String insertUnite = "INSERT INTO Unite(Nom) VALUES (";
            String insertIngredient = "INSERT INTO Ingredient(Nom, UniteID) VALUES (";
            String insertIngredientSecondPart = "(SELECT UniteID from Unite WHERE Unite.Nom = ";



            String insertRecettePart1 = "INSERT INTO Recette(Nom, Duree, NbPersonnes, TypePlatID, CategorieID, Preparation) VALUES (";
            String insertRecettePart2 = "( Select TypePlatID from TypePlat WHERE TypePlat.Nom = ";
            String insertRecettePart3 = "( Select CategorieID from Categorie WHERE Categorie.Nom = ";

            //
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0 -> { //1er colonne
                            recetteName = nextCell.getStringCellValue();
                            System.out.print(recetteName +" : ");
                        }
                        case 1 -> { //2eme colonne
                            categorie = nextCell.getStringCellValue();
                            System.out.println(categorie);
                        }
                        case 2 -> {
                            type = nextCell.getStringCellValue();
                            System.out.println(type);
                        }
                        case 3 -> {
                            duree = nextCell.getNumericCellValue();
                            System.out.println(duree);
                        }
                        case 4 -> {
                            nbPersonne = nextCell.getNumericCellValue();
                            System.out.println(nbPersonne);
                        }
                        case 5 -> {
                            preparation = nextCell.getStringCellValue();
                            System.out.println(preparation);
                        }
                        default -> {
                        }
                    }

                    //replace string

                    //String queryUnite = insertUnite + "'" +ingredientUnit+"'" + ")";
                    //String queryIngredient = insertIngredient + "'" +ingredientName+"'" + "," +insertIngredientSecondPart +"'" + ingredientUnit+"'"  + "))";
                }
                String queryRecette = insertRecettePart1 + "'"+ recetteName + "', " +duree+", "+ nbPersonne +","+ insertRecettePart2 +"'"+ type+ "')," + insertRecettePart3+ "'"+ categorie+"'),\""+preparation+"\")";
                db.sendRequest(queryRecette);
                //String queryIngredientUpdate = updateIngredientPart1 + "'"+ ingredientUnit +"' ) "+ updateIngredientPart2 +"'"+ingredientType+"' "+updateIngredientPart3;
                //System.out.println(queryIngredientUpdate );
            }
            // execute the remaining queries

            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (IOException ex1) {
            System.out.println("Error reading file");
            Path test = Paths.get(excelFilePath);
            System.out.println(test.toAbsolutePath());
            ex1.printStackTrace();
        } catch (Exception e){
            System.out.println(e);
        }

    }

}
