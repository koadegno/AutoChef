package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

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
    static private final Configuration configuration = Configuration.getCurrent();
    static private RecipeTypeDao recipeTypeDao;
    static private final String meal = "Plat";


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = TestConstante.databaseName;
        configuration.setDatabase(databaseName);
        User basicUser = new User("Caius","mot de passe",true);
        configuration.setCurrentUser(basicUser);
        recipeTypeDao = configuration.getRecipeTypeDao();
        recipeTypeDao.insert(meal);

    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> types = recipeTypeDao.getAllName();
        assertEquals(meal, types.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        String simmered = "Mijoté";
        recipeTypeDao.insert(simmered);
        List<String> types = recipeTypeDao.getAllName();
        assertEquals(simmered, types.get(1));
    }
}