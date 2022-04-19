package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestRecipeCategoryDao {

    static private final String fish = "Poison";


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductFamilyDao().insert(fish);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        ArrayList<String> categories = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(fish, categories.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String meat = "Viande";
        Configuration.getCurrent().getProductFamilyDao().insert(meat);
        ArrayList<String> categories = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(meat, categories.get(1));
    }
}