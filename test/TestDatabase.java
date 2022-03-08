import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabase {
    private static Database db;

    @BeforeAll
    static public void createDB() throws SQLException {

        db = new Database("test.sqlite");
        db.insertCategory("Poisson");
        db.insertCategory("Viande");


        db.insertType("Plat");
        db.insertType("Mijoté");
        Recipe bolo = new Recipe("Bolognaise", 60, "Viande", "Mijoté",4, "Cuire des pâtes, oignons, tomates, ail, basilic");
        Recipe carbo = new Recipe("Carbonara", 60, "Poisson", "Plat",5, "Cuire des pâtes, poisson");
        Recipe pesto = new Recipe("Pesto", 20, "Poisson", "Plat",3, "Cuire des pâtes, poisson");

        db.insertRecipe(bolo);
        db.insertRecipe(carbo);
        db.insertRecipe(pesto);
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

//    @Test
//    public void testCreateFamilleAliment(){
//        db.createTableFamilleAliment();
//        String query = "SELECT Nom FROM FamilleAliment;";
//        Boolean res = db.sendRequest(query);
//        assertEquals(res, true);
//    }


    @Test
    public void testGetAllCategories() throws SQLException {
        ArrayList<String> res = db.getAllCategories();
        assertEquals(2, res.size());
    }

    @Test
    public void testGetRecipeWhereCategorieIsMeat() throws SQLException {
        ArrayList<Recipe> res = db.getRecipeWhereCategorie("Viande");
        assertEquals(1 , res.size());
    }

    @Test
    public void testGetRecipeWhereCategorieIsFish() throws SQLException {
        ArrayList<Recipe> res = db.getRecipeWhereCategorie("Poisson");
        assertEquals(2 , res.size());
    }

}