import org.junit.jupiter.api.*;
import ulb.infof307.g01.cuisine.*;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestMenu {

    static private Menu menu = new Menu("Menu Test");
    static private Recipe[] recipes;
    static private Product[] products;

    static Database db;

    @BeforeAll
    static void setUp() throws SQLException {
        recipes  = new Recipe [2];
        products = new Product[2];

        products[0] = new Product("Abricot");
        products[1] = new Product("Fraise");

        recipes[0] = new Recipe(4, "test1", 3, "Vegan", "Test", 2, "Avant le code");
        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        recipes[1] = new Recipe(5, "test2", 5, "Vegan", "Test", 2, "Avant le code");
        recipes[1].add(products[0]);
        recipes[1].add(products[1]);

        createDB();
    }

    static public void createDB() throws SQLException {

        db = new Database("test.sqlite");

        db.insertCategory("Poisson");
        db.insertCategory("Viande");
        db.insertCategory("Végétarien");
        db.insertCategory("Vegan");
        db.insertType("Plat");
        db.insertType("Mijoté");
        db.insertType("Test");

        Recipe bolo  = new Recipe(1, "Bolognaise", 60, "Viande", "Mijoté",4, "Cuire des pâtes, oignons, tomates, ail, basilic");
        Recipe carbo = new Recipe(2, "Carbonara", 60, "Poisson", "Plat",5, "Cuire des pâtes, poisson");
        Recipe pesto = new Recipe(3, "Pesto", 20, "Poisson", "Plat",3, "Cuire des pâtes, poisson");

        db.insertRecipe(bolo);
        db.insertRecipe(carbo);
        db.insertRecipe(pesto);
        db.insertRecipe(recipes[0]);
        db.insertRecipe(recipes[1]);
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @BeforeEach
    private void reset() {
        new Menu("Menu Test");

        menu.addMealTo(Day.Monday, recipes[0]);
        menu.addMealTo(Day.Wednesday, recipes[0]);

        menu.addMealTo(Day.Monday, recipes[1]);
        menu.addMealTo(Day.Friday, recipes[1]);
    }

    @AfterEach
    private void tearDown() {
        menu.clearDay(Day.Monday);
        menu.clearDay(Day.Wednesday);
        menu.clearDay(Day.Friday);
    }


    @Test
    void getMealsfor() {
        int i =0;
        for (Recipe recipeTest : menu.getMealsfor(Day.Monday)) {
                assertEquals(recipes[i], recipeTest);
                i++;
        }
    }

    @Test
    void addMealTo() {
        menu.addMealTo(Day.Monday, recipes[1]);

        List<Recipe> recipesList = menu.getMealsfor(Day.Monday);

        int counter = 0;

        for (Recipe r : recipesList) {
            if (r == recipes[1])
                counter++;
        }

        assertEquals(2, counter);
        // Reset
        menu.removeMealFrom(Day.Monday, recipes[1]);
    }

    @Test
    void removeMealFrom() {
        menu.removeMealFrom(Day.Monday, recipes[1]);

        List<Recipe> recipesList = menu.getMealsfor(Day.Monday);
        int counter = 0;
        for (Recipe r : recipesList) {
            if (r == recipes[1])
                counter++;
        }

        assertEquals(0, counter);
        // Reset
        menu.addMealTo(Day.Monday, recipes[1]);
    }

    @Test
    void replaceMeal() {

        menu.addMealTo(Day.Monday, recipes[1]);
        menu.replaceMeal(Day.Monday, recipes[1], recipes[0]);

        List<Recipe> recipesList = menu.getMealsfor(Day.Monday);

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
        assertEquals(0, menu.getMealsfor(Day.Monday).size());

        menu.clearDay(Day.Wednesday);
        assertEquals(0, menu.getMealsfor(Day.Wednesday).size());

        menu.clearDay(Day.Friday);
        assertEquals(0, menu.getMealsfor(Day.Friday).size());
    }

    @Test
    void generateShoppingList() {

        ShoppingList generatedShoppingList = menu.generateShoppingList();

        System.out.println(generatedShoppingList);
        assertEquals(2, generatedShoppingList.size());

        int[] productsCounter = {0, 0};

        int i = 0;
        for (Product p : generatedShoppingList) {
            productsCounter[i] = p.getQuantity();
            System.out.println(p.getQuantity());
            i++;
        }

        assertEquals(5, productsCounter[0]);
        assertEquals(2, productsCounter[1]);
    }

    @Test
    void generateMenu() throws SQLException {

        menu.generateMenu(db, 1, 3, 6);

        for (Day day: Day.values()) {
            List<Recipe> recipes = menu.getMealsfor(day);
            assertEquals(2, recipes.size());
        }
    }
}