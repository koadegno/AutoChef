package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.database.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test du DAO du type de recette
 */
class TestRecipeTypeDao {
    static private final String meal = "Plat";

    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getRecipeTypeDao().insert(meal);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> types = Configuration.getCurrent().getRecipeTypeDao().getAllName();
        assertEquals(meal, types.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String simmered = "Mijoté";
        Configuration.getCurrent().getRecipeTypeDao().insert(simmered);
        List<String> types = Configuration.getCurrent().getRecipeTypeDao().getAllName();
        assertEquals(simmered, types.get(1));
    }
}