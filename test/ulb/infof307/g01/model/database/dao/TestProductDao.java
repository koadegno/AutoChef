package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test du DAO produit
 */
class TestProductDao {
    static private final String fruit = "Fruit";
    static private final String gram = "g";
    static private final Configuration configuration = Configuration.getCurrent();
    static private ProductDao productDao;
    static private final Product peach = TestConstante.PEACH;
    static private final Product strawberry = TestConstante.STRAWBERRY;

    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = TestConstante.databaseName;
        configuration.setDatabase(databaseName);
        configuration.getProductUnityDao().insert(gram);
        configuration.getProductFamilyDao().insert(fruit);
        configuration.getProductDao().insert(peach);
        productDao = configuration.getProductDao();
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(TestConstante.databaseName));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> products = productDao.getAllName();
        assertEquals(peach.getName(), products.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        productDao.insert(strawberry);
        Product strawberry2 = productDao.get(strawberry.getName());
        assertEquals(strawberry.getName(), strawberry2.getName());
        assertEquals(strawberry.getNameUnity(), strawberry2.getNameUnity());
        assertEquals(strawberry.getFamilyProduct(), strawberry2.getFamilyProduct());
    }

    @Test
    void testGet() throws SQLException {
        Product peach2 = productDao.get(peach.getName());
        assertEquals(peach.getName(), peach2.getName());
        assertEquals(peach.getNameUnity(), peach2.getNameUnity());
        assertEquals(peach.getFamilyProduct(), peach2.getFamilyProduct());
    }
}