package ulb.infof307.g01.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;
import ulb.infof307.g01.model.database.dao.RecipeDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test de l'auto-completion
 */
class TestAutoCompletion {

    private static final Configuration configuration= Configuration.getCurrent();
    public static final String MESSAGE = "Test si recette utilise autant de fois que necessaire";
    public static final String MESSAGE1 = "Test si nombre de categries adequat";
    //private static final String databaseName = "test.sqlite";

    @BeforeAll
    static public void setUp() throws SQLException{
        //createDB();
        TestConstante.createDefaultDB();

        RecipeDao recipeDao = configuration.getRecipeDao();
        recipeDao.insert(TestConstante.BOLO_RECIPE);
        recipeDao.insert(TestConstante.CARBO_RECIPE);
        recipeDao.insert(TestConstante.PESTO_RECIPE);
        recipeDao.insert(TestConstante.TIRAMISU_RECIPE);
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(TestConstante.databaseName));
    }


    @Test
    public void TestGenerateRecipesList() throws SQLException {

        ArrayList<Recipe> recipesAlreadyUsed     = new ArrayList<>();
        HashMap<String, Integer> testRecipes      = new HashMap<>();
        HashMap<String, Integer> categoriesWanted = new HashMap<>();

        categoriesWanted.put(TestConstante.FOOD_CATEGORY_VEGE, 3);
        categoriesWanted.put(TestConstante.FOOD_CATEGORY_MEAT, 2);
        categoriesWanted.put(TestConstante.FOOD_CATEGORY_FISH, 2);
        testRecipes.put(TestConstante.FOOD_CATEGORY_MEAT, 0);
        testRecipes.put(TestConstante.FOOD_CATEGORY_FISH, 0);
        testRecipes.put(TestConstante.FOOD_CATEGORY_VEGE, 0);
        testRecipes.put(TestConstante.BOLOGNAISE_NAME, 0);
        testRecipes.put(TestConstante.CARBONARA_NAME, 0);
        testRecipes.put(TestConstante.PESTO_NAME, 0);
        testRecipes.put(TestConstante.TIRAMISU_NAME, 0);

        List<Recipe> recipes;
        AutoCompletion autoCompletion = new AutoCompletion();
        recipes = autoCompletion.generateRecipesList(recipesAlreadyUsed,categoriesWanted, 7, null);

        // Enumére les catégories et les recettes utilisées dans la HashMap
        for (Recipe recipe : recipes) {
            int valCategory = testRecipes.get(recipe.getCategory());
            int valName     = testRecipes.get(recipe.getName());
            testRecipes.replace(recipe.getCategory(), ++valCategory);
            testRecipes.replace(recipe.getName(), ++valName);
        }

        assertEquals(2, testRecipes.get(TestConstante.FOOD_CATEGORY_MEAT), MESSAGE1);
        assertEquals(2, testRecipes.get(TestConstante.FOOD_CATEGORY_FISH),    MESSAGE1);
        assertEquals(3, testRecipes.get(TestConstante.FOOD_CATEGORY_VEGE), MESSAGE1);
        assertEquals(2, testRecipes.get(TestConstante.BOLOGNAISE_NAME), MESSAGE);
        assertEquals(1, testRecipes.get(TestConstante.CARBONARA_NAME), MESSAGE);
        assertEquals(1, testRecipes.get(TestConstante.PESTO_NAME), MESSAGE);
        assertEquals(3, testRecipes.get(TestConstante.TIRAMISU_NAME), MESSAGE);
    }


    @Test
    void TestFindMax() {

        HashMap<String, Integer> recipes = new HashMap<>();
        AutoCompletion autoCompletion= new AutoCompletion();
        assertNull(autoCompletion.findMax(recipes),
                "Test si HashMap donne est vide");

        recipes.put(TestConstante.FOOD_CATEGORY_FISH, 1);
        recipes.put(TestConstante.FOOD_CATEGORY_MEAT, 10);
        recipes.put("Vegetarien", 3);

        assertEquals(TestConstante.FOOD_CATEGORY_MEAT,
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