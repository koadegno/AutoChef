package ulb.infof307.g01.model.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

/**
 * Test du singleton Configuration
 */
class TestConfiguration {
    private static final String databaseName = "test.sqlite";
    private static Configuration configuration = Configuration.getCurrent();
    static private final Product peach = TestConstante.PEACH;

    @BeforeAll
    static public void initConfig() throws SQLException {

        configuration.setDatabase(databaseName);

        configuration.getProductFamilyDao().insert(peach.getFamilyProduct());
        configuration.getProductUnityDao().insert(peach.getNameUnity());
        configuration.getProductDao().insert(peach);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void testGetProductFamilyDao() throws SQLException {
        List<String> families = configuration.getProductFamilyDao().getAllName();
        assertEquals(peach.getFamilyProduct(), families.get(0));
    }

    @Test
    void testGetProductUnityDao() throws SQLException {
        List<String> unities = configuration.getProductUnityDao().getAllName();
        assertEquals(peach.getNameUnity(), unities.get(0));
    }

    @Test
    void testGetProductDao() throws SQLException {
        Product product = configuration.getProductDao().get(peach.getName());
        assertEquals(peach.getName(), product.getName());
        assertEquals(peach.getQuantity(), product.getQuantity());
        assertEquals(peach.getNameUnity(),product.getNameUnity());
        assertEquals(peach.getFamilyProduct(), product.getFamilyProduct());
    }
}