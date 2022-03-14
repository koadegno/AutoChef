import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.AutoCompletion;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestAutoCompletion {

    static Database db;
    static Recipe bolo     = new Recipe(1, "Bolognaise",60, "Viande", "Plat",4, "Cuire des pâtes, oignons, tomates, ail, basilic");
    static Recipe carbo    = new Recipe(2, "Carbonara",60, "Poisson", "Plat",5, "Cuire des pâtes, poisson");
    static Recipe pesto    = new Recipe(3, "Pesto",    20, "Poisson", "Plat",3, "Cuire des pâtes, poisson");
    static Recipe tiramisu = new Recipe(4, "Tiramisu", 20, "Végétarien", "Dessert",3, "Preparer la mascarpone");


    @BeforeAll
    static public void createDB() throws SQLException {

        db = new Database("test.sqlite");

        db.insertCategory("Poisson");
        db.insertCategory("Viande");
        db.insertCategory("Végétarien");
        db.insertCategory("Vegan");

        db.insertType("Plat");
        db.insertType("Dessert");

        db.insertRecipe(bolo);
        db.insertRecipe(carbo);
        db.insertRecipe(pesto);
        db.insertRecipe(tiramisu);
    }


    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }


    @Test
    public void TestGenerateRecipesList() throws SQLException {

        ArrayList<Recipe> recipesAllReadyUsed     = new ArrayList<>();
        HashMap<String, Integer> testRecipes      = new HashMap<>();
        HashMap<String, Integer> categoriesWanted = new HashMap<>();

        categoriesWanted.put("Végétarien", 3);
        categoriesWanted.put("Viande", 2);
        categoriesWanted.put("Poisson", 2);
        testRecipes.put("Viande", 0);
        testRecipes.put("Poisson", 0);
        testRecipes.put("Végétarien", 0);
        testRecipes.put("Bolognaise", 0);
        testRecipes.put("Carbonara", 0);
        testRecipes.put("Pesto", 0);
        testRecipes.put("Tiramisu", 0);

        List<Recipe> recipes = AutoCompletion.generateRecipesList(recipesAllReadyUsed,categoriesWanted, 7, null,  db);

        // Enumére les catégories et les recettes utilisées dans la HashMap
        for (Recipe recipe : recipes) {
            String category = recipe.getCategory();
            String name     = recipe.getName();
            int valCategory = testRecipes.get(category);
            int valName     = testRecipes.get(name);
            testRecipes.replace(category, ++valCategory);
            testRecipes.replace(name, ++valName);
        }

        assertEquals(2, testRecipes.get("Viande"),     "Test si nombre de categries adequat");
        assertEquals(2, testRecipes.get("Poisson"),    "Test si nombre de categories adequat");
        assertEquals(3, testRecipes.get("Végétarien"), "Test si nombre de categories adequat");
        assertEquals(2, testRecipes.get("Bolognaise"), "Test si recette utilise autant de fois que necessaire");
        assertEquals(1, testRecipes.get("Carbonara"),  "Test si recette utilise autant de fois que necessaire");
        assertEquals(1, testRecipes.get("Pesto"),      "Test si recette utilise autant de fois que necessaire");
        assertEquals(3, testRecipes.get("Tiramisu"),   "Test si recette utilise autant de fois que necessaire");
    }


    @Test
    void TestFindMax() {

        HashMap<String, Integer> recipes = new HashMap<>();
        assertNull(AutoCompletion.findMax(recipes), "Test si HashMap donne est vide");

        recipes.put("Poisson", 1);
        recipes.put("Viande", 10);
        recipes.put("Vegetarien", 3);
        recipes.put("Poulet", 7);
        assertEquals("Viande",AutoCompletion.findMax(recipes), "Test si bonne Key trouver");
    }


    @Test
    public void TestChoiceRecipe() {

        ArrayList<Recipe> recipes             = new ArrayList<>();
        ArrayList<Recipe> recipesAllReadyUsed = new ArrayList<>();
        ArrayList<Recipe> empty               = new ArrayList<>();

        recipes.add(bolo);
        recipes.add(tiramisu);
        recipes.add(carbo);
        recipes.add(pesto);
        recipesAllReadyUsed.add(bolo);
        recipesAllReadyUsed.add(tiramisu);
        recipesAllReadyUsed.add(carbo);

        assertNull(AutoCompletion.choiceRecipe(empty, recipesAllReadyUsed),            "Test si choix de recette vide");
        assertEquals(pesto, AutoCompletion.choiceRecipe(recipes, recipesAllReadyUsed), "Test si choix de la bonne recette");
    }
}