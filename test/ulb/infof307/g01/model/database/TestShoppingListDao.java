package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test du DAO de la liste de course
 */
class TestShoppingListDao {
    static private final String fruit = "Fruit";
    static private final String gram = "g";

    static private final Product peach = new Product("peche", 1, gram, fruit);
    static private final Product strawberry = new Product( "fraise", 1, gram, fruit);

    static private final ShoppingList halloween = new ShoppingList("Halloween");
    static private final ShoppingList christmas = new ShoppingList("Noël");
    static private final ShoppingList easter = new ShoppingList("Pâques");


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

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
        List<String> shoppingList = Configuration.getCurrent().getShoppingListDao().getAllName();
        assertEquals(halloween.getName(), shoppingList.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        Configuration.getCurrent().getShoppingListDao().insert(christmas);
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(christmas.getName());
        assertEquals(christmas.getName(), shoppingList2.getName());

        assertEquals(christmas, shoppingList2);
    }

    @Test
    void testGet() throws SQLException {
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(halloween.getName());
        assertEquals(halloween.getName(), shoppingList2.getName());

        assertEquals(halloween, shoppingList2);
    }

    @Test
    void testUpdate() throws SQLException {
        easter.add(strawberry);
        Configuration.getCurrent().getShoppingListDao().update(easter);
        ShoppingList shoppingList2 = Configuration.getCurrent().getShoppingListDao().get(easter.getName());
        assertEquals(easter.getName(), shoppingList2.getName());

        assertEquals(easter, shoppingList2);
    }
}