import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.cuisine.*;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestMenu {

    static private Menu menu = new Menu("Menu Test");
    static private Recipe[] recipes;
    static private Product[] products;
    static Database db;

    @BeforeAll
    static void setUp() throws SQLException {

        recipes  = new Recipe [7];
        products = new Product[2];

        products[0] = new Product("Abricot");
        products[1] = new Product("Fraise");

        recipes[0] = new Recipe(4, "test1", 3, "Vegan", "Test", 2, "Avant le code");
        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        recipes[1] = new Recipe(5, "test2", 5, "Vegan", "Test", 2, "Avant le code");
        recipes[1].add(products[0]);
        recipes[1].add(products[1]);

        recipes[2]  = new Recipe(1, "Bolognaise", 60, "Viande", "Mijoté",4, "Cuire des pâtes, oignons, tomates, ail, basilic");
        recipes[3]  = new Recipe(2, "Carbonara", 60, "Poisson", "Plat",5, "Cuire des pâtes, poisson");
        recipes[4]  = new Recipe(3, "Pesto", 20, "Poisson", "Plat",3, "Cuire des pâtes, poisson");
        recipes[5]  = new Recipe(6, "test1", 3, "Vegan", "Test", 2, "Avant le code");
        recipes[6]  = new Recipe(7, "test2", 5, "Vegan", "Test", 2, "Avant le code");

        createDB();
    }

    static public void createDB() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        Configuration.getCurrent().getRecipeCategoryDao().insert("Poisson");
        Configuration.getCurrent().getRecipeCategoryDao().insert("Viande");
        Configuration.getCurrent().getRecipeCategoryDao().insert("Végétarien");
        Configuration.getCurrent().getRecipeCategoryDao().insert("Vegan");

        Configuration.getCurrent().getRecipeTypeDao().insert("Plat");
        Configuration.getCurrent().getRecipeTypeDao().insert("Mijoté");
        Configuration.getCurrent().getRecipeTypeDao().insert("Test");

        Configuration.getCurrent().getRecipeDao().insert(recipes[5]);
        Configuration.getCurrent().getRecipeDao().insert(recipes[6]);
        Configuration.getCurrent().getRecipeDao().insert(recipes[2]);
        Configuration.getCurrent().getRecipeDao().insert(recipes[3]);
        Configuration.getCurrent().getRecipeDao().insert(recipes[4]);
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @BeforeEach
    private void reset() {
        new Menu("Menu Test");

        menu.addRecipeTo(Day.Monday, recipes[0]);
        menu.addRecipeTo(Day.Wednesday, recipes[0]);

        menu.addRecipeTo(Day.Monday, recipes[1]);
        menu.addRecipeTo(Day.Friday, recipes[1]);
    }

    @AfterEach
    private void tearDown() {
        menu.clearDay(Day.Monday);
        menu.clearDay(Day.Wednesday);
        menu.clearDay(Day.Friday);
    }


    @Test
    void getRecipesfor() {
        int i =0;
        for (Recipe recipeTest : menu.getRecipesfor(Day.Monday)) {
                assertEquals(recipes[i], recipeTest);
                i++;
        }
    }

    @Test
    void addRecipesTo() {
        menu.addRecipeTo(Day.Monday, recipes[1]);

        List<Recipe> recipesList = menu.getRecipesfor(Day.Monday);

        int counter = 0;

        for (Recipe r : recipesList) {
            if (r == recipes[1])
                counter++;
        }

        assertEquals(2, counter);
        // Reset
        menu.removeRecipeFrom(Day.Monday, recipes[1]);
    }

    @Test
    void removeRecipesFrom() {
        menu.removeRecipeFrom(Day.Monday, recipes[1]);

        List<Recipe> recipesList = menu.getRecipesfor(Day.Monday);
        int counter = 0;
        for (Recipe r : recipesList) {
            if (r == recipes[1])
                counter++;
        }

        assertEquals(0, counter);
        // Reset
        menu.addRecipeTo(Day.Monday, recipes[1]);
    }

    @Test
    void replaceRecipes() {

        menu.addRecipeTo(Day.Monday, recipes[1]);
        menu.replaceRecipe(Day.Monday, recipes[1], recipes[0]);

        List<Recipe> recipesList = menu.getRecipesfor(Day.Monday);

        int counterRecipe0 = 0;
        int counterRecipe1 = 0;
        for (Recipe r : recipesList) {
            if (r == recipes[0])
                counterRecipe0++;
        }
        assertEquals(2, counterRecipe0);
        assertEquals(0, counterRecipe1);
    }

    @Test
    void clearDay() {

        menu.clearDay(Day.Monday);
        assertEquals(0, menu.getRecipesfor(Day.Monday).size());

        menu.clearDay(Day.Wednesday);
        assertEquals(0, menu.getRecipesfor(Day.Wednesday).size());

        menu.clearDay(Day.Friday);
        assertEquals(0, menu.getRecipesfor(Day.Friday).size());
    }

    @Test
    void generateShoppingList() {

        ShoppingList generatedShoppingList = menu.generateShoppingList();

        assertEquals(2, generatedShoppingList.size());

        int[] productsCounter = {0, 0};

        int i = 0;
        for (Product p : generatedShoppingList) {
            productsCounter[i] = p.getQuantity();
            i++;
        }

        assertEquals(5, productsCounter[0]);
        assertEquals(2, productsCounter[1]);
    }

    @Test
    void generateMenu() throws SQLException {

        menu.generateMenu(6, 4, 6);

        for (Day day: Day.values()) {
            List<Recipe> recipes = menu.getRecipesfor(day);
            assertEquals(3, recipes.size());
        }
    }
}