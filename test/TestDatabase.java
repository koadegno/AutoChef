import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.*;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabase {
    private static Database db;
    static private Menu menu = new Menu("Menu Test");
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
        db.createAndGetIdShoppingList("Halloween");
        db.createAndGetIdShoppingList("noel");
        db.createAndGetIdShoppingList("ete");
        db.insertUnite("g");
        db.insertUnite("litres");
        db.insertFamilleAliment("Fruit");
        db.insertIngredient("peche","Fruit","g");
        db.insertIngredient("fraise","Fruit","g");

        menu.addMealTo(Day.Monday, bolo);
        menu.addMealTo(Day.Wednesday, carbo);
        menu.addMealTo(Day.Monday,bolo);
        menu.addMealTo(Day.Friday, pesto);

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
    public void testGetAllTypes() throws SQLException {
        ArrayList<String> res = db.getAllTypes();
        assertEquals(2, res.size());
    }


    @Test
    public void testGetRecipeWhereCategorieIsMeat() throws SQLException {
        ArrayList<Recipe> res = db.getRecipeWhere("Viande",null,0);
        assertEquals(1 , res.size(),"Test nombre recette pour categorie viande");
        assertEquals(res.get(0).getName(),"Bolognaise","Test nom de cette recette");
        assertEquals(res.get(0).getDuration(),60,"Test la duree de la preparation");
        assertEquals(res.get(0).getCategory(),"Viande","Test categorie de la recette");
        assertEquals(res.get(0).getType(),"Mijoté","Test Type de la recette");
        assertEquals(res.get(0).getNbrPerson(),4,"test le nombre de personne");
        assertEquals(res.get(0).getPreparation(),"Cuire des pâtes, oignons, tomates, ail, basilic","Test la preparation");

    }

    @Test
    public void testGetAllShoppingListNameWith3() throws SQLException {
        ArrayList<String> shoppingListName = db.getAllShoppingListName();
        assertEquals(3,shoppingListName.size());
    }

    @Test
    public void testSaveModifyShoppingListDelete() throws SQLException {
        db.createAndGetIdShoppingList("delete");
        ShoppingList rename = new ShoppingList("delete2",4);
        db.saveModifyShoppingList(rename);
        ArrayList<String> shoppingListName = db.getAllShoppingListName();
        assertEquals(3,shoppingListName.size());
    }

    @Test
    public void testSaveModifyShoppingList() throws SQLException {
        ShoppingList rename = new ShoppingList("hiver",3);
        rename.add(new Product("peche"));
        db.saveModifyShoppingList(rename);
        ArrayList<String> shoppingListName = db.getAllShoppingListName();
        assertEquals("hiver",shoppingListName.get(2));
    }

    @Test
    public void testGetAllProductName() throws SQLException {
        ArrayList<String> names = db.getAllProductName();
        assertEquals(2,names.size(),"test nombre produit");
        assertEquals("peche",names.get(0));

    }

    @Test
    public void testGetAllUniteName() throws SQLException {
        ArrayList<String> names = db.getAllUniteName();
        assertEquals(2,names.size(),"test nombre produit");
        assertEquals("g",names.get(0));
    }

    @Test
    public void testGetShoppingListFromName() throws SQLException {
        ShoppingList shoppingList = db.getShoppingListFromName("Halloween");
        shoppingList.add(new Product("peche",5,"g","Fruit"));
        shoppingList.add(new Product("fraise",6,"g","Fruit"));
        db.saveModifyShoppingList(shoppingList);
        ShoppingList newShoppingList = db.getShoppingListFromName("Halloween");
        assertEquals("Halloween",newShoppingList.getName(),"test name shoppingList");
        assertEquals("peche",newShoppingList.get(0).getName(),"test name 1er produit");
        assertEquals(6,newShoppingList.get(1).getQuantity(),"test quantite 2eme produit");
        assertEquals("g",newShoppingList.get(0).getNameUnity(),"test unite 1er produit");
    }

    @Test
    public void testGetRecipeWhere() throws SQLException {
        ArrayList<Recipe> res = db.getRecipeWhere("Poisson", "Plat", 3);
        assertEquals(1 , res.size());
    }

    @Test
    public void testGetRecipeWhere2Null() throws SQLException {
        ArrayList<Recipe> res = db.getRecipeWhere(null,null,0);
        assertEquals(3 , res.size());
    }

    @Test
    public void testInsertNewMenu()throws SQLException{
        db.saveNewMenu(menu);
        Menu newMenu = db.getMenuFromName("Menu Test");
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getMealsfor(day);
            List<Recipe> recipeFromMenu = menu.getMealsfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
                assertEquals(recipeFromMenu.get(i).getName(),recipeFromNewMenu.get(i).getName());
                assertEquals(recipeFromMenu.get(i).getDuration(),recipeFromNewMenu.get(i).getDuration());
                assertEquals(recipeFromMenu.get(i).getNbrPerson(),recipeFromNewMenu.get(i).getNbrPerson());
                assertEquals(recipeFromMenu.get(i).getPreparation(),recipeFromNewMenu.get(i).getPreparation());
                assertEquals(recipeFromMenu.get(i).getType(),recipeFromNewMenu.get(i).getType());
                assertEquals(recipeFromMenu.get(i).getCategory(),recipeFromNewMenu.get(i).getCategory());
            }
        }
    }

}