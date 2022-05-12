package ulb.infof307.g01.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import ulb.infof307.g01.model.*;

/**
 * test de la classe shoppingList
 */
class TestShoppingList {

    static private ShoppingList list;
    static private Vector<Product> testList;

    @BeforeAll
    static public void createList() {
        list = new ShoppingList("Liste de course pour Noël");

        Product testProduct = new Product("Dinde");
        Product testProduct2 = new Product("Orange");

        testList = new Vector<>(2);
        testList.add(testProduct);
        testList.add(testProduct2);
    }

    @BeforeEach
    private void addElements() {
        list.add(testList.get(0));
        list.add(testList.get(0));
        list.add(testList.get(1));
    }

    @AfterEach
    private void tearDown() {
        list.clear();
    }

    @Test
    void add() {
        Product product = new Product("Dinde");

        assertTrue(list.contains(product));
        assertTrue(list.contains(testList.get(1)));


        // Element en double
        for (Product p : list) {
            if (p.equals(testList.get(0)))
                assertEquals(p.getQuantity(), 2);
            if (p.equals(testList.get(1)))
                assertEquals(p.getQuantity(), 1);
        }

    }

    @Test
    void remove() {
        assertTrue(list.remove(testList.get(0)), "ShoppingList.remove() n'a pas renvoyé True");
        assertTrue(list.contains(testList.get(0)), "L'élément a été supprimé alors qu'il n'aurait pas dû (Quantité > 1)");

        for (Product p : list) {
            if (p.equals(testList.get(0)))
                assertEquals(1, p.getQuantity(), "La quantité du produit list[0] n'a pas été décrémenté après suppression");
        }

        assertTrue(list.remove(testList.get(0)), "ShoppingList.remove() n'a pas renvoyé True");
        assertFalse(list.contains(testList.get(0)),
                "Le produit list[0] n'as pas été supprimé avec une quantité de 0");

        assertFalse(list.remove(new Product("false")),
                "ShoppingList.remove() n'a pas renvoyé False avec un objet non présent dans la liste");
    }

}