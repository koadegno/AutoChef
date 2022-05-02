package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Recipe;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipe {

    private Recipe recipeTest;

    @BeforeEach
    private void createRecipe() {recipeTest = new Recipe(1, "Test", 3, "Informatique",
        "Test", 2, "Faire le test avant le code");
    }

    @Test
    public void testEquals() {
        Recipe recipeTestCopy = new Recipe(1, "Test", 3, "Informatique",
                "Test", 2, "Faire le test avant le code");
        assertEquals(recipeTest, recipeTestCopy, "Echec du test de comparaison valide");

        Recipe recipeTestInvalid = new Recipe(2, "Invalid", 3, "Informatique",
                "Test", 2, "Faire le test avant le code");
        assertNotEquals(recipeTest, recipeTestInvalid, "Echec du test de comparaison invalide");
    }
}
