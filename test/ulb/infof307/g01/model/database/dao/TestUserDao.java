package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test du DAO de l'utilisateur
 */
class TestUserDao {
    private static final Address userAddress = new Address("Empire Romain","Rome",1180,"Rue l'empereur",20);
    private static final User basicUser = new User(-1,"Caius","Augustus","Caligula2","mot de passe",userAddress,true);
    private static final String DATABASE_NAME = TestConstante.databaseName;
    static private final Configuration configuration = Configuration.getCurrent();
    static private UserDao userDao;

    @BeforeAll
    static void startDatabase(){

        configuration.setDatabase(DATABASE_NAME);
        userDao = configuration.getUserDao();
    }

    @AfterAll
    static void deleteDatabase() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(DATABASE_NAME));
    }

    @Test
    void insert() throws SQLException {
        String userPseudo = "Caligula 1";
        insertion(userPseudo);
        User userInserted = userDao.get(userPseudo);
        assertEquals(basicUser,userInserted);
    }

    private void insertion(String pseudo) throws SQLException {
        basicUser.setPseudo(pseudo);
        userDao.insert(basicUser);

    }

    @Test
    void get() throws SQLException {
        String userPseudo = "Caligula 2";
        insertion(userPseudo);
        User userInserted = userDao.get(userPseudo);
        assertEquals(basicUser,userInserted);
    }

}