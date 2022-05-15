package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

/**
 * test du DAO produit
 */
class TestProductDao {
    static private final String fruit = "Fruit";
    static private final String gram = "g";
    
    static private final Product peach = new Product.ProductBuilder().withName("peche").withQuantity(1).withNameUnity(gram).withFamilyProduct(fruit).build();
    static private final Product strawberry = new Product.ProductBuilder().withName("fraise").withQuantity(1).withNameUnity(gram).withFamilyProduct(fruit).build();

    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductUnityDao().insert(gram);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);
        Configuration.getCurrent().getProductDao().insert(peach);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> products = Configuration.getCurrent().getProductDao().getAllName();
        assertEquals(peach.getName(), products.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        Configuration.getCurrent().getProductDao().insert(strawberry);
        Product strawberry2 = Configuration.getCurrent().getProductDao().get(strawberry.getName());
        assertEquals(strawberry.getName(), strawberry2.getName());
        assertEquals(strawberry.getNameUnity(), strawberry2.getNameUnity());
        assertEquals(strawberry.getFamilyProduct(), strawberry2.getFamilyProduct());
    }

    @Test
    void testGet() throws SQLException {
        Product peach2 = Configuration.getCurrent().getProductDao().get(peach.getName());
        assertEquals(peach.getName(), peach2.getName());
        assertEquals(peach.getNameUnity(), peach2.getNameUnity());
        assertEquals(peach.getFamilyProduct(), peach2.getFamilyProduct());
    }
}