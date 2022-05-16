package ulb.infof307.g01.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.AutoCompletion;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de l'auto-completion
 */
class TestAutoCompletion {

    @BeforeAll
    static public void createDB() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_FISH);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_MEAT);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_VEGE);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_VEGAN);

        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_MEAL);
        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_SIMMERED);
        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_DESSERT);

        Configuration.getCurrent().getRecipeDao().insert(TestConstante.BOLO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.CARBO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.PESTO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.TIRAMISU_RECIPE);
    }


    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        Configuration.getCurrent().closeConnection();
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

        List<Recipe> recipes;
        AutoCompletion autoCompletion = new AutoCompletion();
        recipes = autoCompletion.generateRecipesList(recipesAllReadyUsed,categoriesWanted, 7, null);

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
        AutoCompletion autoCompletion= new AutoCompletion();
        assertNull(autoCompletion.findMax(recipes),
                "Test si HashMap donne est vide");

        recipes.put("Poisson", 1);
        recipes.put("Viande", 10);
        recipes.put("Vegetarien", 3);

        assertEquals("Viande",
                autoCompletion.findMax(recipes),
                "Test si bonne Key trouver");
    }


    @Test
    public void TestChoiceRecipe() {

        ArrayList<Recipe> recipes             = new ArrayList<>();
        ArrayList<Recipe> recipesAllReadyUsed = new ArrayList<>();
        ArrayList<Recipe> empty               = new ArrayList<>();

        recipes.add(TestConstante.BOLO_RECIPE);
        recipes.add(TestConstante.TIRAMISU_RECIPE);
        recipes.add(TestConstante.CARBO_RECIPE);
        recipes.add(TestConstante.PESTO_RECIPE);
        recipesAllReadyUsed.add(TestConstante.BOLO_RECIPE);
        recipesAllReadyUsed.add(TestConstante.TIRAMISU_RECIPE);
        recipesAllReadyUsed.add(TestConstante.CARBO_RECIPE);

        AutoCompletion autoCompletion = new AutoCompletion();
        assertNull(autoCompletion.choiceRecipe(empty, recipesAllReadyUsed),            "Test si choix de recette vide");
        assertEquals(TestConstante.PESTO_RECIPE, autoCompletion.choiceRecipe(recipes, recipesAllReadyUsed), "Test si choix de la bonne recette");
    }
}