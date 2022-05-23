package ulb.infof307.g01.model.database.dao;

import org.junit.experimental.categories.Category;
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
 * test du DAO de la catégorie de recette
 */
class TestRecipeCategoryDao {

    static private final String fish = "Poison";
    static private final Configuration configuration = Configuration.getCurrent();
    static private RecipeCategoryDao recipeCategoryDao;


    @BeforeAll
    static public void initConfig() throws SQLException {
        configuration.setDatabase(TestConstante.databaseName);
        recipeCategoryDao = configuration.getRecipeCategoryDao();
        recipeCategoryDao.insert(fish);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> categories = recipeCategoryDao.getAllName();
        assertEquals(fish, categories.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String meat = "Viande";
        recipeCategoryDao.insert(meat);
        List<String> categories = recipeCategoryDao.getAllName();
        assertEquals(meat, categories.get(1));
    }
}