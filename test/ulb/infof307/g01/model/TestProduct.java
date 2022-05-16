package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test de la classe produit
 */
public class TestProduct {

    private Product productTest;

    @BeforeEach
    private void createProduct() {
        productTest =  new Product.ProductBuilder().withName("Produit Test").build();
    }

    @Test
    public void testProduct() {
        assertEquals("Produit Test", productTest.getName());
    }

    @Test
    public void testChangeName() {
        productTest.rename("Changer");
        assertEquals("Changer", productTest.getName());
    }

    @Test
    public void testDecrease() {
        assertFalse(productTest.decreaseQuantity(), "La méthode à renvoyé True avec une réduction inférieure à 1");
        assertEquals(productTest.getQuantity(), 1, "Le produit ne peut pas avoir une quantité inférieure à 1 !");

        productTest.increaseQuantity();
        assertTrue(productTest.decreaseQuantity(), "La quantité a renvoyé False avec un appel valide (réduction de 2 à 1)");
        assertEquals(productTest.getQuantity(), 1, "La quantité du produit n'a pas été réduite correctement'");
    }

    @Test
    public void testIncrease() {
        productTest.increaseQuantity();
        assertEquals(productTest.getQuantity(), 2, "La quantité du produit n'a pas été augmentée");
    }

    @Test
    public void testChangeQuantity() {
        productTest.setQuantity(50);
        assertEquals(50, productTest.getQuantity(), "La quantité n'a pas été modifié correctement");

        productTest.setQuantity(-50);
        assertEquals(1, productTest.getQuantity(), "La quantité n'a pas été réduite à sa valeur " +
                "minimale avec une reduction négative");
    }

    @Test
    public void testNameUnity(){
        assertEquals("Unité", productTest.getNameUnity());
    }

    @Test
    public void testEquals() {
        Product productTestCopy =  new Product.ProductBuilder().withName(productTest.getName()).build();

        assertEquals(productTest, productTestCopy, "Echec du test de comparaison valide");

        productTestCopy.rename("p");
        assertNotEquals(productTest, productTestCopy, "Echec du test de comparaison invalide");
    }
}
