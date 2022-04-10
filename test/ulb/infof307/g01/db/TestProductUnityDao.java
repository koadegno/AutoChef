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

class TestProductUnityDao {

    static String gram = "g";


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductFamilyDao().insert(gram);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        ArrayList<String> unities = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(gram, unities.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String litre = "litre";
        Configuration.getCurrent().getProductFamilyDao().insert(litre);
        ArrayList<String> unities = Configuration.getCurrent().getProductFamilyDao().getAllName();
        assertEquals(litre, unities.get(1));
    }
}