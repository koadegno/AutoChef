package ulb.infof307.g01.model.export;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test de l'exportation en ODT
 */
class TestODTCreator {
    public static final String NAME_SHOPPING_LIST = "test/testShoppingList";
    static private ShoppingList shoppingList;

    @BeforeAll
    static public void createShoppingList() {
        shoppingList =  new ShoppingList(NAME_SHOPPING_LIST);
        Product testProduct = new Product.ProductBuilder().withName("Banane").withQuantity(3).withFamilyProduct(TestConstante.FRUIT).withQuantity(1).withNameUnity("kg").build();
        Product testProduct2 = new Product.ProductBuilder().withName("Boeuf").withQuantity(7).withFamilyProduct(TestConstante.FAMILY_PRODUCT_MEAT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
        Product testProduct3 = new Product.ProductBuilder().withName("Mouton").withQuantity(7).withFamilyProduct(TestConstante.FAMILY_PRODUCT_MEAT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
        Product testProduct4 = new Product.ProductBuilder().withName("Dorade").withQuantity(7).withFamilyProduct(TestConstante.FAMILY_PRODUCT_FISH).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
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
        ShoppingListVector.sort(Comparator.comparing(Product::getFamilyProduct));
        ShoppingListReadVector.sort(Comparator.comparing(Product::getFamilyProduct));



        assertEquals(ShoppingListVector.get(0).getName(),ShoppingListReadVector.get(0).getName());
        assertEquals(ShoppingListVector.get(0).getFamilyProduct(),
                ShoppingListReadVector.get(0).getFamilyProduct());
        assertEquals(ShoppingListVector.get(1).getName(),ShoppingListReadVector.get(1).getName());
        assertEquals(ShoppingListVector.get(1).getFamilyProduct(),
                ShoppingListReadVector.get(1).getFamilyProduct());
        odtReader.close();
    }
}