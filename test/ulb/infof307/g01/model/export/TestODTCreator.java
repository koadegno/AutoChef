package ulb.infof307.g01.model.export;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test de l'exporatation en ODT
 */
class TestODTCreator {
    public static final String NAME_SHOPPING_LIST = "test/testShoppingList";
    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList(NAME_SHOPPING_LIST);
        Product testProduct = new Product("Banane", 3, "kg", "Fruits");
        Product testProduct2 = new Product("Boeuf", 7, "g", "Viande");
        Product testProduct3 = new Product("Mouton", 7, "g", "Viande");
        Product testProduct4 = new Product("Dorade", 1, "kg", "Poison");
        shoppingList.add(testProduct);
        shoppingList.add(testProduct2);
        shoppingList.add(testProduct3);
        shoppingList.add(testProduct4);
    }

    @AfterAll
    static void afterAll() throws IOException {
        Files.deleteIfExists(Path.of(NAME_SHOPPING_LIST+".odt"));
    }

    @Test
    void testCreateODT()  throws Exception {
        ODTCreator odtCreator = new ODTCreator();
        odtCreator.createODT(shoppingList);

        OdfTextDocument odtReader = OdfTextDocument.loadDocument (NAME_SHOPPING_LIST + ".odt");
        ShoppingList shoppingListRead =  odtCreator.readODT(odtReader);
        ArrayList<Product> ShoppingListReadVector = new ArrayList<>(shoppingListRead);
        ArrayList<Product> ShoppingListVector = new ArrayList<>(shoppingList);
        ShoppingListVector.sort(Comparator.comparing(Product::getFamillyProduct));
        ShoppingListReadVector.sort(Comparator.comparing(Product::getFamillyProduct));



        assertEquals(ShoppingListVector.get(0).getName(),ShoppingListReadVector.get(0).getName());
        assertEquals(ShoppingListVector.get(0).getFamillyProduct(),
                ShoppingListReadVector.get(0).getFamillyProduct());
        assertEquals(ShoppingListVector.get(1).getName(),ShoppingListReadVector.get(1).getName());
        assertEquals(ShoppingListVector.get(1).getFamillyProduct(),
                ShoppingListReadVector.get(1).getFamillyProduct());
        odtReader.close();
    }
}