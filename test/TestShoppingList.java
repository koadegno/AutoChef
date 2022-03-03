import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import cuisine.*;

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
    }

    @Test
    void remove() {
        list.remove(testList.get(0));
        assertEquals(testList.get(1), list.get(0));
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