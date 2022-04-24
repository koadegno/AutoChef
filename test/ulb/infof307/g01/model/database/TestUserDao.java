package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

class TestUserDao {
    private static final Address userAddress = new Address("Empire Romain","Rome",1180,"Rue l'empereur",20);
    private static final User BASIC_USER = new User(21,"Caius","Augustus","Caligula2","mot de passe",userAddress,false);;
    private static final String DATABASE_NAME = "test.sqlite";

    @BeforeAll
    static void startDatabase(){
        Configuration.getCurrent().setDatabase(DATABASE_NAME);
    }

    @AfterAll
    static void deleteDatabase() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(DATABASE_NAME));
    }

    @Test
    void insert() throws SQLException {
        Configuration.getCurrent().getUserDao().insert(BASIC_USER);
    }

    @Test
    void update() throws SQLException {
        BASIC_USER.setID(22);
        Configuration.getCurrent().getUserDao().insert(BASIC_USER);
        BASIC_USER.setPseudo("Xenon");
        Configuration.getCurrent().getUserDao().update(BASIC_USER);
    }

    @Test
    void get() throws SQLException {
        Configuration.getCurrent().getUserDao().insert(BASIC_USER);
        User userAdded = Configuration.getCurrent().getUserDao().get(String.valueOf(BASIC_USER.getID()));

    }
}