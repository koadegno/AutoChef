package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test du DAO de la liste de course
 */
class TestShoppingListDao {

    static private final Product peach = TestConstante.PEACH;
    static private final Product strawberry = TestConstante.STRAWBERRY;
    static private final ShoppingList halloween = new ShoppingList("Halloween");
    static private final ShoppingList christmas = new ShoppingList("Noël");
    static private final ShoppingList easter = new ShoppingList("Pâques");
    static private final Configuration configuration = Configuration.getCurrent();
    static private ShoppingListDao shoppingListDao;


    @BeforeAll
    static public void initConfig() throws SQLException {
        configuration.setDatabase(TestConstante.databaseName);
        shoppingListDao = configuration.getShoppingListDao();
        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        configuration.setCurrentUser(testUser);

        configuration.getProductUnityDao().insert(TestConstante.GRAM);
        configuration.getProductFamilyDao().insert(TestConstante.FAMILY_PRODUCT_FRUIT);

        configuration.getProductDao().insert(peach);
        configuration.getProductDao().insert(strawberry);

        halloween.add(peach);
        halloween.add(strawberry);

        christmas.add(strawberry);

        easter.add(peach);

        shoppingListDao.insert(halloween);
        shoppingListDao.insert(easter);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> shoppingList = shoppingListDao.getAllName();
        assertTrue(shoppingList.contains(halloween.getName()));
        assertTrue(shoppingList.contains(easter.getName()));
    }

    @Test
    void testInsert() throws SQLException {
        shoppingListDao.insert(christmas);
        ShoppingList shoppingList2 = shoppingListDao.get(christmas.getName());

        assertEquals(christmas.getName(), shoppingList2.getName());
        assertEquals(christmas, shoppingList2);
    }

    @Test
    void testGet() throws SQLException {
        ShoppingList shoppingList2 = shoppingListDao.get(halloween.getName());
        assertEquals(halloween.getName(), shoppingList2.getName());

        assertEquals(halloween, shoppingList2);
    }

    @Test
    void testUpdate() throws SQLException {
        easter.add(strawberry);
        shoppingListDao.update(easter);
        ShoppingList shoppingList2 = shoppingListDao.get(easter.getName());
        assertEquals(easter.getName(), shoppingList2.getName());

        assertEquals(easter, shoppingList2);
    }
}