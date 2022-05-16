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
    static private final Product peach = new Product.ProductBuilder().withName("peche").withQuantity(1).withFamilyProduct("Fruit").withNameUnity("g").build();
    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        Configuration.getCurrent().getProductFamilyDao().insert(peach.getFamilyProduct());
        Configuration.getCurrent().getProductUnityDao().insert(peach.getNameUnity());
        Configuration.getCurrent().getProductDao().insert(peach);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetProductFamilyDao() throws SQLException {
        List<String> families = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(peach.getFamilyProduct(), families.get(0));
    }

    @Test
    void testGetProductUnityDao() throws SQLException {

        List<String> unities = Configuration.getCurrent().getProductUnityDao().getAllName();
        assertEquals(peach.getNameUnity(), unities.get(0));
    }

    @Test
    void testGetProductDao() throws SQLException {
        Product product = Configuration.getCurrent().getProductDao().get(peach.getName());
        assertEquals(peach.getName(), product.getName());
        assertEquals(peach.getQuantity(), product.getQuantity());
        assertEquals(peach.getNameUnity(),product.getNameUnity());
        assertEquals(peach.getFamilyProduct(), product.getFamilyProduct());
    }
}