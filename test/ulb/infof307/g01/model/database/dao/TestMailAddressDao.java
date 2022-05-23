package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
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
    static private final Configuration configuration = Configuration.getCurrent();
    private final MailAddressDao mailAddressDao = configuration.getMailAddressDao();;
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
        configuration.setDatabase(databaseName);
        configuration.setCurrentUser(basicUser);
        configuration.getUserDao().insert(basicUser);
        configuration.getMailAddressDao().insert(mail1);
        configuration.getMailAddressDao().insert(mail2);
        configuration.getMailAddressDao().insert(mail4);
    }

    @AfterAll
    static public void closeDatabase() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void getAllName() throws SQLException {
        List<String> userMails = mailAddressDao.getAllName();
        assertTrue(userMails.contains(mail4));
        assertTrue(userMails.contains(mail1));
        assertTrue(userMails.contains(mail2));
    }


    @Test
    void insertForUser() throws SQLException, DuplicatedKeyException {
        mailAddressDao.insert(mail5);
        List<String> allMails = mailAddressDao.getAllName();
        assertTrue(allMails.contains(mail4));
        assertTrue(allMails.contains(mail5));
    }

    @Test
    void insertForUserDuplicateAddress() throws SQLException, DuplicatedKeyException {
        try {
            mailAddressDao.insert(mail1);
            fail();
        }catch (DuplicatedKeyException e){
            List<String> allMails = mailAddressDao.getAllName();
            assertTrue(allMails.contains(mail1));
        }
    }

    @Test
    void insert() throws SQLException, DuplicatedKeyException {
        mailAddressDao.insert(mail3);
        int mailInsertedID = Integer.parseInt(mailAddressDao.get(mail3));
        String mailInserted = mailAddressDao.get(mailInsertedID);
        assertEquals(mail3,mailInserted);
    }

    @Test
    void get() throws SQLException {
        String mailID = mailAddressDao.get(mail1);
        assertEquals(mail1ID,mailID);
    }

}