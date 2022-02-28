import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabase {
    private static Database db;

    @BeforeAll
    static public void createDB(){
        db = new Database("test.sqlite");
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    public void testCreateFamilleAliment(){
        db.createTableFamilleAliment();
        String query = "SELECT Nom FROM FamilleAliment;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testNotCreateSameTableTwice(){
        boolean result = db.createTableFamilleAliment();
        assertFalse(result);
    }

    @Test
    public void testCreateUnite(){
        db.createTableUnite();
        String query = "SELECT Nom FROM Unite;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testCreateCategorie(){
        db.createTableCategorie();
        String query = "SELECT Nom FROM Categorie;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testCreateTypePlat(){
        db.createTableTypePlat();
        String query = "SELECT Nom FROM TypePlat;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testCreateRecette(){
        db.createTableRecette();
        String query = "SELECT Nom FROM Recette;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testCreateIngredient(){
        db.createTableIngredient();
        String query = "SELECT Nom FROM Ingredient;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testCreateRecetteIngredient(){
        db.createTableRecetteIngredient();
        String query = "SELECT RecetteID FROM RecetteIngredient;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    // ------------------------------------------------------------
    @Test
    public void testCreateListeCourse(){
        db.createTableListeCourse();
        String query = "SELECT ListeCourseID FROM ListeCourse;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    // ------------------------------------------------------------

    @Test
    public void testCreateListeCourseIngredient(){
        db.createTableListeCourseIngredient();
        String query = "SELECT ListeCourseID FROM ListeCourseIngredient;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testCreateMenuRecette(){
        db.createTableMenuRecette();
        String query = "SELECT Date, Heure FROM MenuRecette;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }
    //-------------------------------------------------------------


    @Test
    public void testCreateIdShoppingList(){
        db.createTableListeCourse();
        assertEquals(1,db.createAndGetIdShoppingList("'noel'"));
    }

    @Test
    public void testInsert() throws SQLException {
        String[] name = {"null","'litres'"};
        db.insert("Unite",name);
        String query = "SELECT UniteID FROM Unite WHERE Nom = 'litres'";
        ResultSet res = db.sendQuery(query);
        res.next();
        int id = res.getInt("UniteID");
        assertEquals(1,id);
    }

    @Test
    public void testSelect() throws SQLException {
        String[] name = {"Nom"};
        String[] values = {"'litres'"};
        String[] signs = {"="};
        ResultSet res = db.select("Unite",name,values,signs);
        res.next();
        int id = res.getInt("UniteID");
        assertEquals(1,id);
    }

}