package ulb.infof307.g01.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

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
        for (int i = 0; i < christmas.size(); i++) {
            assertEquals(christmas.get(i).getQuantity(), shoppingList2.get(i).getQuantity());
            assertEquals(christmas.get(i).getName(), shoppingList2.get(i).getName());
            assertEquals(christmas.get(i).getNameUnity(), shoppingList2.get(i).getNameUnity());
            assertEquals(christmas.get(i).getFamillyProduct(), shoppingList2.get(i).getFamillyProduct());
        }
    }

    @Test
    void testGet() throws SQLException {
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(halloween.getName());
        assertEquals(halloween.getName(), shoppingList2.getName());
        for (int i = 0; i < halloween.size(); i++) {
            assertEquals(halloween.get(i).getQuantity(), shoppingList2.get(i).getQuantity());
            assertEquals(halloween.get(i).getName(), shoppingList2.get(i).getName());
            assertEquals(halloween.get(i).getNameUnity(), shoppingList2.get(i).getNameUnity());
            assertEquals(halloween.get(i).getFamillyProduct(), shoppingList2.get(i).getFamillyProduct());
        }
    }

    @Test
    void testUpdate() throws SQLException {
        easter.add(strawberry);
        Configuration.getCurrent().getShoppingListDao().update(easter);
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(easter.getName());
        assertEquals(easter.getName(), shoppingList2.getName());
        for (int i = 0; i < easter.size(); i++) {
            assertEquals(easter.get(i).getQuantity(), shoppingList2.get(i).getQuantity());
            assertEquals(easter.get(i).getName(), shoppingList2.get(i).getName());
            assertEquals(easter.get(i).getNameUnity(), shoppingList2.get(i).getNameUnity());
            assertEquals(easter.get(i).getFamillyProduct(), shoppingList2.get(i).getFamillyProduct());
        }
    }
}