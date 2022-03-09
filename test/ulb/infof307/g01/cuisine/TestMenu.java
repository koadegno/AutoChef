package ulb.infof307.g01.cuisine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestMenu {

    private Menu menu = new Menu("");

    static private Recipe[] recipes;
    static private Product[] products;

    @BeforeAll
    private void setUp() {
        recipes[0] = new Recipe(0, "", 0, "", "", 5, "");

        products[0] = new Product("Abricot");

        recipes[0].add(products[0]);
        recipes[0].add(products[0]);

        menu.addMealTo(Day.Monday, recipes[0]);
        menu.addMealTo(Day.Wednesday, recipes[0]);

        recipes[1] = new Recipe(0, "", 0, "", "", 0, "");
        recipes[1].add(products[0]);
        products[1] = new Product("Fraise");
        recipes[1].add(products[1]);

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

    }

    @Test
    void addMealTo() {

    }

    @Test
    void removeMealFrom() {
    }

    @Test
    void replaceMeal() {
    }

    @Test
    void clearDay() {
    }

    @Test
    void generateShoppingList() {
    }
}