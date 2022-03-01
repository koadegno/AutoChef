import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testReduire() {
        assertFalse(produitTest.reduire(), "La méthode à renvoyé True avec une réduction inférieure à 1");
        assertEquals(produitTest.getQuantite(), 1, "Le produit ne peut pas avoir une quantité inférieure à 1 !");

        produitTest.augmenter();
        assertTrue(produitTest.reduire(), "La quantité a renvoyé False avec un appel valide (réduction de 2 à 1)");
        assertEquals(produitTest.getQuantite(), 1, "La quantité du produit n'a pas été réduite correctement'");
    }

    @Test
    public void testAugmenter() {
        produitTest.augmenter();
        assertEquals(produitTest.getQuantite(), 2, "La quantité du produit n'a pas été augmentée");
    }

    @Test
    public void testChangeQuantite() {
        produitTest.changeQuantite(50);
        assertEquals(50, produitTest.getQuantite(), "La quantité n'a pas été modifié correctement");

        produitTest.changeQuantite(-50);
        assertEquals(1, produitTest.getQuantite(), "La quantité n'a pas été réduite à sa valeur " +
                "minimale avec une reduction négative");
    }

    @Test
    public void testEquals() {
        Produit produitTestCopy = new Produit(produitTest.getNom());

        assertEquals(produitTest, produitTestCopy, "Echec du test de comparaison valide");

        produitTestCopy.renommer("p");
        assertNotEquals(produitTest, produitTestCopy, "Echec du test de comparaison invalide");
    }
}
