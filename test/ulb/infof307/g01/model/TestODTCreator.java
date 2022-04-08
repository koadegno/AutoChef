package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestODTCreator {

    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList("test/testShoppingList");
        Product testProduct = new Product("Banane", 3, "kg", "Fruits");
        Product testProduct2 = new Product("Carotte", 7, "g", "Viande");
        shoppingList.add(testProduct);
        shoppingList.add(testProduct2);
    }

    @Test
    void testCreateODT()  throws Exception {
        ODTCreator odtCreator = new ODTCreator();
        odtCreator.createODT(shoppingList);
        String text = """
            1. Liste de courses : test/testShoppingList
            1.1. Fruits
            Banane 3kg
            1.2. Viande
            Carotte 7g""";

        OdfTextDocument odtReader = OdfTextDocument.loadDocument ("test/testShoppingList.odt");
        ShoppingList shoppingListRead =  odtCreator.readODT(odtReader);
        Vector<Product> ShoppingListReadVector = new Vector<>(shoppingListRead);
        Vector<Product> ShoppingListVector = new Vector<>(shoppingList);

        assertEquals(ShoppingListVector.get(0).getName(),ShoppingListReadVector.get(0).getName());
        assertEquals(ShoppingListVector.get(0).getFamillyProduct(),
                ShoppingListReadVector.get(0).getFamillyProduct());
        assertEquals(ShoppingListVector.get(1).getName(),ShoppingListReadVector.get(1).getName());
        assertEquals(ShoppingListVector.get(1).getFamillyProduct(),
                ShoppingListReadVector.get(1).getFamillyProduct());
        odtReader.close();


    }
}