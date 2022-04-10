package ulb.infof307.g01.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.db.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestProductFamilyDao {

    static String fruit = "Fruit";


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        ArrayList<String> families = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(fruit, families.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String vegetable = "Légume";
        Configuration.getCurrent().getProductFamilyDao().insert(vegetable);
        ArrayList<String> families = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(vegetable, families.get(1));
    }
}