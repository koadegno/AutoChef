package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test du DAO d'unité lié a un produit
 */
class TestProductUnityDao {

    static private final String gram = "g";
    static private final Configuration configuration = Configuration.getCurrent();
    static private ProductUnityDao productUnityDao;


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        configuration.setDatabase(databaseName);
        productUnityDao = configuration.getProductUnityDao();
        productUnityDao.insert(gram);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(TestConstante.databaseName));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> unities = productUnityDao.getAllName();
        assertEquals(gram, unities.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String litre = "litre";
        productUnityDao.insert(litre);
        List<String> unities = productUnityDao.getAllName();
        assertEquals(litre, unities.get(1));
    }
}