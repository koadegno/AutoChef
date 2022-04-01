import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import ulb.infof307.g01.model.*;

class TestShoppingList {

    static private ShoppingList list;
    static private Vector<Product> testList;

    @BeforeAll
    static public void createList() {
        list = new ShoppingList("Liste de course pour Noël");

        Product testProduct = new Product("Dinde");
        Product testProduct2 = new Product("Orange");

        testList = new Vector<Product>(2);
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
        list.removeAllElements();
    }

    @Test
    void add() {
        assertEquals(testList.get(0), list.get(0));
        assertEquals(testList.get(1), list.get(1));

        // Element in double
        assertEquals(2 , list.get(0).getQuantity());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {list.get(2);});
    }

    @Test
    void remove() {
        assertTrue(list.remove(testList.get(0)), "ShoppingList.remove() n'a pas renvoyé True");
        assertEquals(1, list.get(0).getQuantity(),
                "La quantité du produit list[0] n'a pas été décrémenté après suppression");

        assertTrue(list.remove(testList.get(0)), "ShoppingList.remove() n'a pas renvoyé True");
        assertEquals(testList.get(1), list.get(0),
                "Le produit list[0] n'as pas été supprimé avec une quantité de 0");

        assertFalse(list.remove(new Product("false")),
                "ShoppingList.remove() n'a pas renvoyé False avec un objet non présent dans la liste");
    }

    @Test
    void setName() {
        String testName = "Test";
        list.setName(testName);

        assertEquals(testName, list.getName());
    }

    @Test
    void setArchived() {
        assertFalse(list.isArchived());

        list.setArchived(true);

        assertTrue(list.isArchived());
    }
}