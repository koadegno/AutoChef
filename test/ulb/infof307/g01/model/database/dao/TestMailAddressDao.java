package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.exception.DuplicatedKeyException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test du dao pour l'adresse mail
 */
class TestMailAddressDao {
    static private final String mail1 = "mail1@mail.be";
    static private final String mail1ID = "1";
    static private final String mail2 = "mail2@ulb.ac.be";
    static private final String mail3 = "mail3@google.be";
    static private final String mail4 = "xenon@google.be";
    static private final String mail5 = "xenon2@google.be";
    static private final String databaseName = "test.sqlite";
    private static final Address userAddress = new Address("Empire Romain","Rome",1180,"Rue l'empereur",20);
    private static final User basicUser = new User(-1,"Caius","Augustus","Caligula2","mot de passe",userAddress,true);


    @BeforeAll
    static public void setUp() throws SQLException, DuplicatedKeyException {
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().setCurrentUser(basicUser);
        Configuration.getCurrent().getUserDao().insert(basicUser);
        Configuration.getCurrent().getMailAddressDao().insert(mail1);
        Configuration.getCurrent().getMailAddressDao().insert(mail2);
        Configuration.getCurrent().getMailAddressDao().insert(mail4);
    }

    @AfterAll
    static public void closeDatabase() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void getAllName() throws SQLException {
        List<String> userMails = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertTrue(userMails.contains(mail4));
        assertTrue(userMails.contains(mail1));
        assertTrue(userMails.contains(mail2));
    }


    @Test
    void insertForUser() throws SQLException, DuplicatedKeyException {
        Configuration.getCurrent().getMailAddressDao().insert(mail5);
        List<String> allMails = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertTrue(allMails.contains(mail4));
        assertTrue(allMails.contains(mail5));
    }

    @Test
    void insertForUserDuplicateAddress() throws SQLException {
        try {
            Configuration.getCurrent().getMailAddressDao().insert(mail1);
        } catch (DuplicatedKeyException e) {
        }
        List<String> allMails = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertTrue(allMails.contains(mail1));
    }

    @Test
    void insert() throws SQLException, DuplicatedKeyException {
        Configuration.getCurrent().getMailAddressDao().insert(mail3);
        int mailInsertedID = Integer.parseInt(Configuration.getCurrent().getMailAddressDao().get(mail3));
        String mailInserted = Configuration.getCurrent().getMailAddressDao().get(mailInsertedID);
        assertEquals(mail3,mailInserted);
    }

    @Test
    void get() throws SQLException {
        String mailID = Configuration.getCurrent().getMailAddressDao().get(mail1);
        assertEquals(mail1ID,mailID);
    }

}