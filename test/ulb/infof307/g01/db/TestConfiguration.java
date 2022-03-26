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
import java.util.ArrayList;


class TestConfiguration {
    static private Product peche = new Product("peche", 1,"g", "Fruit");


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        Configuration.getCurrent().getProductFamilyDao().insert(peche.getFamillyProduct());
        Configuration.getCurrent().getProductUnityDao().insert(peche.getNameUnity());
        Configuration.getCurrent().getProductDao().insert(peche);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetProductFamilyDao() throws SQLException {
        ArrayList<String> families = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(peche.getFamillyProduct(), families.get(0));
    }

    @Test
    void testGetProductUnityDao() throws SQLException {

        ArrayList<String> unities = Configuration.getCurrent().getProductUnityDao().getAllName();
        assertEquals(peche.getNameUnity(), unities.get(0));
    }

    @Test
    void testGetProductDao() throws SQLException {
        Product product = Configuration.getCurrent().getProductDao().get(peche.getName());
        assertEquals(peche.getName(), product.getName());
        assertEquals(peche.getQuantity(), product.getQuantity());
        assertEquals(peche.getNameUnity(),product.getNameUnity());
        assertEquals(peche.getFamillyProduct(), product.getFamillyProduct());
    }



}