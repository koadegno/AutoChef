package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test du dao pour l'adresse mail
 */
class TestMailAddressDao {
    static private final String mail1 = "mail1@mail.be";
    static private final String mail1ID = "1";
    static private final String mail2 = "mail2@ulb.ac.be";
    static private final String mail3 = "mail3@google.be";
    static private final String FAVORIS_MAIL = "xenon@google.be";
    static private final String FAVORIS_MAIL2 = "xenon2@google.be";
    static private final String databaseName = "test.sqlite";
    private static final Address userAddress = new Address("Empire Romain","Rome",1180,"Rue l'empereur",20);
    private static final int USER_ID = 22;
    private static final User basicUser = new User(USER_ID,"Caius","Augustus","Caligula2","mot de passe",userAddress,true);
    private static int numberFavoriteMail = 1;


    @BeforeAll
    static public void setUp() throws SQLException {
        Configuration.getCurrent().setCurrentUser(basicUser);
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getUserDao().insert(basicUser);
        Configuration.getCurrent().getMailAddressDao().insert(mail1);
        Configuration.getCurrent().getMailAddressDao().insert(mail2);
        Configuration.getCurrent().getMailAddressDao().insertUserMail(FAVORIS_MAIL);
    }

    @AfterAll
    static public void closeDatabase() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void getAllName() throws SQLException {
        List<String> favorisMail = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertEquals(numberFavoriteMail,favorisMail.size());
        assertTrue(favorisMail.contains(FAVORIS_MAIL));
    }


    @Test
    void insertForUser() throws SQLException {
        Configuration.getCurrent().getMailAddressDao().insertUserMail(FAVORIS_MAIL2);
        List<String> favorisMail = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertEquals(++numberFavoriteMail ,favorisMail.size());
        assertEquals(FAVORIS_MAIL,favorisMail.get(0));
        assertEquals(FAVORIS_MAIL2,favorisMail.get(1));
    }

    @Test
    void insertForUserDuplicateAddress() throws SQLException {
        Configuration.getCurrent().getMailAddressDao().insertUserMail(mail1);
        List<String> favorisMail = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertEquals(++numberFavoriteMail,favorisMail.size());
        assertEquals(mail1,favorisMail.get(0));
    }

    @Test
    void insert() throws SQLException {
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