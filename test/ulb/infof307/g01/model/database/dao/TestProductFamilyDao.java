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
 * test du DAO type de produit
 */
class TestProductFamilyDao {

    static private final String fruit = "Fruit";
    private static final Configuration configuration = Configuration.getCurrent();
    private static ProductFamilyDao productFamilyDao;
    


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = TestConstante.databaseName;
        configuration.setDatabase(databaseName);
        productFamilyDao = configuration.getProductFamilyDao();
        productFamilyDao.insert(fruit);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> families = productFamilyDao.getAllName();
        assertEquals(fruit, families.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String vegetable = "Légume";
        productFamilyDao.insert(vegetable);
        List<String> families = productFamilyDao.getAllName();
        assertEquals(vegetable, families.get(1));
    }
}