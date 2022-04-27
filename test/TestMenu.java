import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMenu {

    static private final Menu menu = new Menu("Menu Test");
    static private Recipe[] recipes;
    static private Product[] products;

    /**
     * Stocke la quantité contenu dans une recette pour chaque produit
     *  {@code RECIPE_PRODUCT_QUANTITY[i][j]} contient la quantité du {@link Product} {@code products[j]}
     *  contenu dans la {@link Recipe} {@code recipes[i]}
     */
    private static final int[][] RECIPE_PRODUCT_QUANTITY = new int[2][2];

    /**
     * Stocke la quantité contenu dans {@code menu} pour chaque {@code products}
     *  {@code MENU_PRODUCT_QUANTITY[i]} contient la quantité du {@link Product} {@code products[i]}
     *  contenu dans {@code menu}
     */
    private static final int[]   MENU_PRODUCT_QUANTITY = new int[2];


    @BeforeAll
    static void setUp() throws SQLException {

        recipes  = new Recipe [7];
        products = new Product[2];



        products[0] = new Product("Abricot");
        products[1] = new Product("Fraise");

        recipes[0] = new Recipe(4, "test1", 3, "Vegan", "Test", 2, "Avant le code");
        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        RECIPE_PRODUCT_QUANTITY[0][0] = 2;

        recipes[1] = new Recipe(5, "test2", 5, "Vegan", "Test", 2, "Avant le code");
        recipes[1].add(products[0]);
        recipes[1].add(products[1]);

        RECIPE_PRODUCT_QUANTITY[1][0] = 1;
        RECIPE_PRODUCT_QUANTITY[1][1] = 1;

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

        User testUser = new User("admin","admin",true);
        testUser.setID(1);
        Configuration.getCurrent().setCurrentUser(testUser);

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
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @BeforeEach
    private void reset() {
        new Menu("Menu Test");

        menu.addRecipeTo(Day.Monday, recipes[0]);
        menu.addRecipeTo(Day.Wednesday, recipes[0]);

        menu.addRecipeTo(Day.Monday, recipes[1]);
        menu.addRecipeTo(Day.Friday, recipes[1]);

        MENU_PRODUCT_QUANTITY[0] = (RECIPE_PRODUCT_QUANTITY[0][0] + RECIPE_PRODUCT_QUANTITY[1][0] ) * 2;
        MENU_PRODUCT_QUANTITY[1] = (RECIPE_PRODUCT_QUANTITY[0][1] + RECIPE_PRODUCT_QUANTITY[1][1] ) * 2;
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

        assertEquals(products.length, generatedShoppingList.size());

        int[] productsCounter = new int[products.length];

        for (Product p : generatedShoppingList) {
            for (int i = 0; i < products.length; i++) {
                if (p.equals(products[i])) {
                    productsCounter[i] = p.getQuantity();
                }
            }
        }

        assertEquals(MENU_PRODUCT_QUANTITY[0], productsCounter[0]);
        assertEquals(MENU_PRODUCT_QUANTITY[1], productsCounter[1]);
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