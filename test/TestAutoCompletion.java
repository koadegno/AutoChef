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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestAutoCompletion {

    static Database db;

    @BeforeAll
    static public void createDB() throws SQLException {

        db = new Database("test.sqlite");

        db.insertCategory("Poisson");
        db.insertCategory("Viande");
        db.insertType("Plat");
        db.insertType("Mijoté");

        Recipe bolo  = new Recipe(1, "Bolognaise", 60, "Viande", "Mijoté",4, "Cuire des pâtes, oignons, tomates, ail, basilic");
        Recipe carbo = new Recipe(2, "Carbonara", 60, "Poisson", "Plat",5, "Cuire des pâtes, poisson");
        Recipe pesto = new Recipe(3, "Pesto", 20, "Poisson", "Plat",3, "Cuire des pâtes, poisson");

        db.insertRecipe(bolo);
        db.insertRecipe(carbo);
        db.insertRecipe(pesto);
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    public void TestGenerateMenu() throws SQLException, IOException {

        ArrayList<Recipe> recipesAllReadyUsed = new ArrayList<>();
        HashMap<String, Integer> testRecipes = new HashMap<>();
        testRecipes.put("Viande", 0);
        testRecipes.put("Poisson", 0);
        testRecipes.put("Bolognaise", 0);
        testRecipes.put("Carbonara", 0);
        testRecipes.put("Pesto", 0);

        List<Recipe> recipes = AutoCompletion.generateMenu(recipesAllReadyUsed,4, db);

        for (Recipe recipe : recipes) {

            String category = recipe.getCategory();
            String name     = recipe.getName();

            int valCategory = testRecipes.get(category);
            int valName     = testRecipes.get(name);

            testRecipes.replace(category, ++valCategory);
            testRecipes.replace(name, ++valName);
        }

        assertEquals(2, testRecipes.get("Viande"));
        assertEquals(2, testRecipes.get("Poisson"));
        assertEquals(2, testRecipes.get("Bolognaise"));
        assertEquals(1, testRecipes.get("Carbonara"));
        assertEquals(1, testRecipes.get("Pesto"));
    }

    @Test
    void TestFindMin() {

        HashMap<String, Integer> recipes = new HashMap<>();
        recipes.put("Poisson", 1);
        recipes.put("Viande", 10);
        recipes.put("Vegetarien", 3);
        recipes.put("Poulet", 7);

        assertEquals("Poisson",AutoCompletion.findMin(recipes));
    }

    @Test
    public void TestChoiceRecipe() {

        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Recipe> recipesAllReadyUsed = new ArrayList<>();

        Recipe bolognaise = new Recipe(1, "Bolognaise", 45, "Viande",
                "Plat principal", 1, "Faire le test avant le code");
        Recipe bolognaiseVegan = new Recipe(2, "Bolognaise", 45, "Vegan",
                "Plat principal", 1, "Faire le test avant le code");
        Recipe soupePotiron = new Recipe(3, "Soupe potiron", 15, "Vegan",
                "Soupe", 2, "Faire le test avant le code");
        Recipe margherita = new Recipe(4, "Margherita", 45, "Vegan",
                "Plat principal", 1, "Faire le test avant le code");
        Recipe lasagne = new Recipe(5, "lasagne", 45, "Viande",
                "Plat principal", 1, "Faire le test avant le code");

        recipes.add(bolognaise);
        recipes.add(bolognaiseVegan);
        recipes.add(soupePotiron);
        recipes.add(margherita);
        recipes.add(lasagne);

        recipesAllReadyUsed.add(bolognaise);
        recipesAllReadyUsed.add(bolognaiseVegan);
        recipesAllReadyUsed.add(soupePotiron);
        recipesAllReadyUsed.add(margherita);

        assertEquals(lasagne, AutoCompletion.choiceRecipe(recipes, recipesAllReadyUsed));
    }
}