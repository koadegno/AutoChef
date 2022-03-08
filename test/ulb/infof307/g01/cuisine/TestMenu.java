package ulb.infof307.g01.cuisine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestMenu {

    private Menu menu = new Menu("");

    @BeforeEach
    private void setUp() {
        Recipe recipe = new Recipe(0, "", 0, "", "", 5, "");

        menu.addMealTo(Day.Monday, recipe);
    }
    @AfterEach


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