package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Recipe;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test de la classe recipe
 */
public class TestRecipe {

    private Recipe recipeTest;

    @BeforeEach
    private void createRecipe() {recipeTest = new Recipe.RecipeBuilder().withId(1).withName("Test").withDuration(3).withCategory("Informatique").withType("Test").withNumberOfPerson(2).withPreparation("Faire le test avant le code").build();
    }

    @Test
    public void testEquals() {

        Recipe recipeTestCopy = new Recipe.RecipeBuilder().withId(1).withName("Test").withDuration(3).withCategory("Informatique").withType("Test").withNumberOfPerson(2).withPreparation("Faire le test avant le code").build();
        assertEquals(recipeTest, recipeTestCopy, "Echec du test de comparaison valide");

        Recipe recipeTestInvalid = new Recipe.RecipeBuilder().withId(2).withName("Invalid").withDuration(3).withCategory("Informatique").withType("Test").withNumberOfPerson(2).withPreparation("Faire le test avant le code").build();
        assertNotEquals(recipeTest, recipeTestInvalid, "Echec du test de comparaison invalide");
    }
}
