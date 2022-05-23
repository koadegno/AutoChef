package ulb.infof307.g01.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Recipe;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test de la classe recipe
 */
public class TestRecipe {

    private static Recipe recipeTest;

    @BeforeAll
    static public void createRecipe(){
        recipeTest = new Recipe.RecipeBuilder()
                .withId(1)
                .withName("Test")
                .withDuration(3)
                .withCategory("Informatique")
                .withType("Test")
                .withNumberOfPerson(2)
                .withPreparation("Faire le test avant le code").build();
    }

    //TODO: Test add and remove??

    @Test
    public void testEquals() {
        Recipe recipeTestCopy = new Recipe.RecipeBuilder()
                .withId(1).withName("Test")
                .withDuration(3)
                .withCategory("Informatique")
                .withType("Test")
                .withNumberOfPerson(2)
                .withPreparation("Faire le test avant le code").build();

        Recipe recipeTestInvalid = new Recipe.RecipeBuilder()
                .withId(2).withName("Invalid")
                .withDuration(3)
                .withCategory("Informatique")
                .withType("Test")
                .withNumberOfPerson(2)
                .withPreparation("Faire le test avant le code").build();


        boolean isEqual = recipeTest.equals(recipeTestCopy);
        boolean isNotEqual = !recipeTest.equals(recipeTestInvalid);

        assertTrue(isEqual, "Echec du test de comparaison valide");
        assertTrue(isNotEqual, "Echec du test de comparaison invalide");
    }
}
