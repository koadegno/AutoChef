package ulb.infof307.g01.db;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestShoppingListDao {
    static String fruit = "Fruit";
    static String gram = "g";

    static private Product peach = new Product("peche", 1, gram, fruit);
    static private Product strawberry = new Product( "fraise", 1, gram, fruit);

    static private ShoppingList halloween = new ShoppingList("Halloween");
    static private ShoppingList christmas = new ShoppingList("Noël");
    static private ShoppingList easter = new ShoppingList("Pâques");


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        Configuration.getCurrent().getProductUnityDao().insert(gram);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);

        Configuration.getCurrent().getProductDao().insert(peach);
        Configuration.getCurrent().getProductDao().insert(strawberry);

        halloween.add(peach);
        halloween.add(strawberry);

        christmas.add(strawberry);

        easter.add(peach);

        Configuration.getCurrent().getShoppingListDao().insert(halloween);
        Configuration.getCurrent().getShoppingListDao().insert(easter);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        ArrayList<String> shoppingList = Configuration.getCurrent().getShoppingListDao().getAllName();
        assertEquals(halloween.getName(), shoppingList.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        Configuration.getCurrent().getShoppingListDao().insert(christmas);
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(christmas.getName());
        assertEquals(christmas.getName(), shoppingList2.getName());

        Iterator<Product> iteratorChristmas  = sortedSet(christmas);
        Iterator<Product> iteratorShoppingList2 = sortedSet(shoppingList2);

       while (iteratorChristmas.hasNext() && iteratorShoppingList2.hasNext()) {
           Product nextChristmasProduct = iteratorChristmas.next();
           Product nextShoppingList2Product = iteratorShoppingList2.next();

           assertEquals(nextChristmasProduct.getQuantity(), nextShoppingList2Product.getQuantity());
           assertEquals(nextChristmasProduct.getName(), nextShoppingList2Product.getName());
           assertEquals(nextChristmasProduct.getNameUnity(), nextShoppingList2Product.getNameUnity());
           assertEquals(nextChristmasProduct.getFamillyProduct(), nextShoppingList2Product.getFamillyProduct());
        }
    }

    @Test
    void testGet() throws SQLException {
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(halloween.getName());
        assertEquals(halloween.getName(), shoppingList2.getName());

        Iterator<Product> iteratorHalloween     = sortedSet(halloween);
        Iterator<Product> iteratorShoppingList2 = sortedSet(shoppingList2);
        while (iteratorHalloween.hasNext() && iteratorShoppingList2.hasNext()) {
            Product nextHalloweenProduct = iteratorHalloween.next();
            Product nextShoppingList2Product = iteratorShoppingList2.next();

            assertEquals(nextHalloweenProduct.getQuantity(), nextShoppingList2Product.getQuantity());
            assertEquals(nextHalloweenProduct.getName(), nextShoppingList2Product.getName());
            assertEquals(nextHalloweenProduct.getNameUnity(), nextShoppingList2Product.getNameUnity());
            assertEquals(nextHalloweenProduct.getFamillyProduct(), nextShoppingList2Product.getFamillyProduct());
        }
    }

    @Test
    void testUpdate() throws SQLException {
        easter.add(strawberry);
        Configuration.getCurrent().getShoppingListDao().update(easter);
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(easter.getName());
        assertEquals(easter.getName(), shoppingList2.getName());

        Iterator<Product> iteratorEaster    = sortedSet(easter);
        Iterator<Product> iteratorShoppingList2 = sortedSet(shoppingList2);

        while (iteratorEaster.hasNext() && iteratorShoppingList2.hasNext()) {
            Product nextEasterProduct        = iteratorEaster.next();
            Product nextShoppingList2Product = iteratorShoppingList2.next();

            assertEquals(nextEasterProduct.getQuantity(), nextShoppingList2Product.getQuantity());
            assertEquals(nextEasterProduct.getName(), nextShoppingList2Product.getName());
            assertEquals(nextEasterProduct.getNameUnity(), nextShoppingList2Product.getNameUnity());
            assertEquals(nextEasterProduct.getFamillyProduct(), nextShoppingList2Product.getFamillyProduct());
        }
    }

    private Iterator<Product> sortedSet(Set<Product> set) {
        List<Product> list = new Vector<>(set);
        list.sort(Comparator.comparing(Product::getName));

        return list.iterator();
    }
}