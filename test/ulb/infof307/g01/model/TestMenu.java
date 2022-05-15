package ulb.infof307.g01.model;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * tests des models menus
 */
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


        recipes[0] = new Recipe.RecipeBuilder().withId(4).withName("test1").withDuration(3).withCategory(TestConstante.FOOD_CATEGORY_VEGAN).withType(TestConstante.FOOD_TYPE_DESSERT).withPreparation("Avant le code").build();
        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        RECIPE_PRODUCT_QUANTITY[0][0] = 2;

        recipes[1] = new Recipe.RecipeBuilder().withId(5).withName("test2").withDuration(5).withCategory(TestConstante.FOOD_CATEGORY_VEGAN).withType(TestConstante.FOOD_TYPE_DESSERT).withPreparation("Avant le code").build();
        recipes[1].add(products[0]);
        recipes[1].add(products[1]);

        RECIPE_PRODUCT_QUANTITY[1][0] = 1;
        RECIPE_PRODUCT_QUANTITY[1][1] = 1;

        recipes[2]  = TestConstante.BOLO_RECIPE;
        recipes[3]  = TestConstante.CARBO_RECIPE;
        recipes[4]  = TestConstante.PESTO_RECIPE;
        recipes[5]  = TestConstante.TIRAMISU_RECIPE;

        createDB();
    }

    static public void createDB() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_MEAT);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_FISH);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_VEGE);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_VEGAN);

        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_MEAL);
        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_SIMMERED);
        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_DESSERT);

        Configuration.getCurrent().getRecipeDao().insert(recipes[5]);
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