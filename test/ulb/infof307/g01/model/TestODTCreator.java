package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestODTCreator {

    public static final String NAME_SHOPPING_LIST = "test/testShoppingList";
    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList(NAME_SHOPPING_LIST);
        Product testProduct = new Product("Banane", 3, "kg", "Fruits");
        Product testProduct2 = new Product("Carotte", 7, "g", "Viande");
        shoppingList.add(testProduct);
        shoppingList.add(testProduct2);
    }

    @Test
    void testCreateODT()  throws Exception {
        ODTCreator odtCreator = new ODTCreator();
        odtCreator.createODT(shoppingList);

        OdfTextDocument odtReader = OdfTextDocument.loadDocument (NAME_SHOPPING_LIST+".odt");
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