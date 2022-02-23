import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProduit {

    private Produit produitTest;

    @BeforeEach
    private void createProduit() {
        produitTest = new Produit("Produit Test");
    }

    @Test
    public void testProduit() {
        assertEquals("Produit Test", produitTest.getNom());
    }

    @Test
    public void testChangeName() {
        produitTest.renommer("Changer");
        assertEquals("Changer", produitTest.getNom());
    }
}
