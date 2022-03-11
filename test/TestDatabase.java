import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.cuisine.ShoppingList;
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
        db.createAndGetIdShoppingList("'Halloween'");
        db.createAndGetIdShoppingList("'noel'");
        db.createAndGetIdShoppingList("'ete'");
        db.insertUnite("g");
        db.insertFamilleAliment("Fruit");
        db.insertIngredient("peche","Fruit","g");
        db.insertIngredient("fraise","Fruit","g");
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
        assertEquals(1 , res.size(),"Test nombre recette pour categorie viande");
        assertEquals(res.get(0).getName(),"Bolognaise","Test nom de cette recette");
        assertEquals(res.get(0).getDuration(),60,"Test la duree de la preparation");
        assertEquals(res.get(0).getCategory(),"Viande","Test categorie de la recette");
        assertEquals(res.get(0).getType(),"Mijoté","Test Type de la recette");
        assertEquals(res.get(0).getNbrPerson(),4,"test le nombre de personne");
        assertEquals(res.get(0).getPreparation(),"Cuire des pâtes, oignons, tomates, ail, basilic","Test la preparation");

    }

    @Test
    public void testGetRecipeWhereCategorieIsFish() throws SQLException {

        ArrayList<Recipe> res = db.getRecipeWhereCategorie("Poisson");
        assertEquals(2 , res.size());
    }


    @Test
    public void testGetAllShoppingListNameWith3() throws SQLException {
        ArrayList<String> shoppingListName = db.getAllShoppingListName();
        assertEquals(3,shoppingListName.size());
    }


    @Test
    public void testSaveModifyShoppingList() throws SQLException {
        ShoppingList rename = new ShoppingList("hiver",3);
        db.saveModifyShoppingList(rename);
        ArrayList<String> shoppingListName = db.getAllShoppingListName();
        assertEquals("hiver",shoppingListName.get(2));
    }

    @Test
    public void TestGetAllProductName() throws SQLException {
        ArrayList<String> names = db.getAllProductName();
        assertEquals(2,names.size(),"test nombre produit");
        assertEquals("peche",names.get(0));

    }

}