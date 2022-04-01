package ulb.infof307.g01.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;


class TestConfiguration {
    static private Product peach = new Product("peche", 1,"g", "Fruit");


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        Configuration.getCurrent().getProductFamilyDao().insert(peach.getFamillyProduct());
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
        assertEquals(peach.getFamillyProduct(), families.get(0));
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
        assertEquals(peach.getFamillyProduct(), product.getFamillyProduct());
    }
}