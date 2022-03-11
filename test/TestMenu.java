import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestMenu {

    static private Menu menu = new Menu("Menu Test");

    static private Recipe[] recipes;
    static private Product[] products;

    @BeforeAll
    static void setUp() {
        recipes  = new Recipe [2];
        products = new Product[2];

        products[0] = new Product("Abricot");
        products[1] = new Product("Fraise");

        recipes[0] = new Recipe(0, "test1", 0, "", "", 0, "");
        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        recipes[1] = new Recipe(0, "test2", 0, "", "", 0, "");
        recipes[1].add(products[0]);
        recipes[1].add(products[1]);
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
}