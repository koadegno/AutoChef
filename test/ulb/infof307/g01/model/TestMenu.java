package ulb.infof307.g01.model;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.database.Configuration;
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
    static private final Configuration configuration = Configuration.getCurrent();
    //static private final String databaseName = "test.sqlite";

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

        products[0] = new Product.ProductBuilder().withName("Abricot").build();
        products[1] =  new Product.ProductBuilder().withName("Fraise").build();

        recipes[0] = new Recipe.RecipeBuilder()
                .withId(4).withName("test1")
                .withDuration(3)
                .withCategory(TestConstante.FOOD_CATEGORY_VEGAN)
                .withType(TestConstante.FOOD_TYPE_DESSERT)
                .withPreparation("Avant le code").build();
        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        RECIPE_PRODUCT_QUANTITY[0][0] = 2;

        recipes[1] = new Recipe.RecipeBuilder()
                .withId(5)
                .withName("test2")
                .withDuration(5)
                .withCategory(TestConstante.FOOD_CATEGORY_VEGAN)
                .withType(TestConstante.FOOD_TYPE_DESSERT)
                .withPreparation("Avant le code").build();
        recipes[1].add(products[0]);
        recipes[1].add(products[1]);

        RECIPE_PRODUCT_QUANTITY[1][0] = 1;
        RECIPE_PRODUCT_QUANTITY[1][1] = 1;

        recipes[2]  = TestConstante.BOLO_RECIPE;
        recipes[3]  = TestConstante.CARBO_RECIPE;
        recipes[4]  = TestConstante.PESTO_RECIPE;
        recipes[5]  = TestConstante.TIRAMISU_RECIPE;

        //createDB();
        TestConstante.createDefaultDB();
        fillDB();
    }

    static public void fillDB()throws SQLException{
        //Ajout des recettes dans la DB
        configuration.getRecipeDao().insert(recipes[5]);
        configuration.getRecipeDao().insert(recipes[2]);
        configuration.getRecipeDao().insert(recipes[3]);
        configuration.getRecipeDao().insert(recipes[4]);
    }


    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(TestConstante.databaseName));
    }

    @BeforeEach
    public void reset() {
        new Menu("Menu Test");

        menu.addRecipeTo(Day.Monday, recipes[0]);
        menu.addRecipeTo(Day.Wednesday, recipes[0]);

        menu.addRecipeTo(Day.Monday, recipes[1]);
        menu.addRecipeTo(Day.Friday, recipes[1]);

        MENU_PRODUCT_QUANTITY[0] = (RECIPE_PRODUCT_QUANTITY[0][0] + RECIPE_PRODUCT_QUANTITY[1][0] ) * 2;
        MENU_PRODUCT_QUANTITY[1] = (RECIPE_PRODUCT_QUANTITY[0][1] + RECIPE_PRODUCT_QUANTITY[1][1] ) * 2;
    }

    @AfterEach
    public void tearDown() {
        menu.clearDay(Day.Monday);
        menu.clearDay(Day.Wednesday);
        menu.clearDay(Day.Friday);
    }


    @Test
    void getRecipesFor() {
        List<Recipe> recipesMonday = menu.getRecipesfor(Day.Monday);
        int i =0;

        for (Recipe recipeTest : recipesMonday) {
                assertEquals(recipes[i], recipeTest);
                i++;
        }
    }

    @Test
    void addRecipesTo() {
        int counter = 0;

        menu.addRecipeTo(Day.Monday, recipes[1]);

        for (Recipe r : menu.getRecipesfor(Day.Monday)) {
            if (r.equals(recipes[1]))
                counter++;
        }
        assertEquals(2, counter);
    }

    @Test
    void removeRecipesFrom() {
        int counter = 0;

        menu.removeRecipeFrom(Day.Monday, recipes[1]);

        for (Recipe r : menu.getRecipesfor(Day.Monday)) {
            if (r == recipes[1])
                counter++;
        }
        assertEquals(0, counter);
    }

    @Test
    void addRemoveRecipes(){
        int counterRecipe0 = 0;
        int counterRecipe1 = 0;

        menu.addRecipeTo(Day.Monday, recipes[0]);
        menu.removeRecipeFrom(Day.Monday, recipes[1]);

        for (Recipe r : menu.getRecipesfor(Day.Monday)) {
            if (r.equals(recipes[0])) counterRecipe0++;
            if (r.equals(recipes[1])) counterRecipe1++;
        }
        assertEquals(2, counterRecipe0);
        assertEquals(0, counterRecipe1);
    }

    @Test
    void replaceRecipes() {
        //FIXME: si on add recipe[1] dans menu, il devrait apparaitre 2 fois, donc assert fails?
        menu.addRecipeTo(Day.Monday, recipes[1]);
        int counterRecipe0 = 0;
        int counterRecipe1 = 0;

        menu.replaceRecipe(Day.Monday, recipes[1], recipes[0]);

        for (Recipe r : menu.getRecipesfor(Day.Monday)) {
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
    }

    @Test
    void generateShoppingList() {
        ShoppingList generatedShoppingList = menu.generateShoppingList();

        //Vérifier la taille de la Liste de courses
        assertEquals(products.length, generatedShoppingList.size());

        //Vérifier que tous les produits de la liste de courses correspondent à ceux du menu
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