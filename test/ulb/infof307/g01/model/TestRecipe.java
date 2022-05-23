package ulb.infof307.g01.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * test de la classe recipe
 */
public class TestRecipe {

    @Test
    public void testEquals() {

        Recipe recipeTest = new Recipe.RecipeBuilder().withId(1).withName("Test").withDuration(3).withCategory("Informatique").withType("Test").withNumberOfPerson(2).withPreparation("Faire le test avant le code").build();

        Recipe recipeTestCopy = new Recipe.RecipeBuilder().withId(1).withName("Test").withDuration(3).withCategory("Informatique").withType("Test").withNumberOfPerson(2).withPreparation("Faire le test avant le code").build();
        assertEquals(recipeTest, recipeTestCopy, "Echec du test de comparaison valide");

        Recipe recipeTestInvalid = new Recipe.RecipeBuilder().withId(2).withName("Invalid").withDuration(3).withCategory("Informatique").withType("Test").withNumberOfPerson(2).withPreparation("Faire le test avant le code").build();
        assertNotEquals(recipeTest, recipeTestInvalid, "Echec du test de comparaison invalide");
    }
}
