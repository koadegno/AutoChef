package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;


class TestUserDao {
    private static final Address userAddress = new Address("Empire Romain","Rome",1180,"Rue l'empereur",20);
    private static final User basicUser = new User(-1,"Caius","Augustus","Caligula2","mot de passe",userAddress,false);;
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
        String userPseudo = "Caligula 1";
        insertion(-1, userPseudo);
        User userInserted = Configuration.getCurrent().getUserDao().get(userPseudo);
        assertEquals(basicUser,userInserted);
    }

    private void insertion(int ID, String pseudo) throws SQLException {
        basicUser.setID(ID);
        basicUser.setPseudo(pseudo);
        Configuration.getCurrent().getUserDao().insert(basicUser);

    }

    @Test
    void get() throws SQLException {
        String userPseudo = "Caligul2";
        insertion(-1, userPseudo);
        User userInserted = Configuration.getCurrent().getUserDao().get(userPseudo);
        assertEquals(basicUser,userInserted);
    }

}