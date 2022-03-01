import static org.junit.jupiter.api.Assertions.*;

public class TestRecipe {

    private Recipe recipeTest;

    @BeforeEach
    private void createRecipe() {Recipe recipeTest = new Recipe(1, "Test", 3, "Informatique",
        "Test", 2, "Faire le test avant le code");
    }

    @Test
    public void testEquals() {
        Recipe recipeTestCopy = new Recipe(1, "Test", 3, "Informatique",
                "Test", 2, "Faire le test avant le code");
        assertEquals(recipeTest, recipeTestCopy, "Echec du test de comparaison valide");
        productTestCopy.rename("p");
        assertNotEquals(recipeTest, recipeTestCopy, "Echec du test de comparaison invalide");
    }
}
