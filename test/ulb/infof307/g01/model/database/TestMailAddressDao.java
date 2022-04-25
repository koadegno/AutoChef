package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    private static final int NUMBER_FAVORIS_MAIL = 1;


    @BeforeAll
    static public void setUp() throws SQLException {
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getUserDao().insert(basicUser);
        Configuration.getCurrent().getMailAddressDao().insert(mail1);
        Configuration.getCurrent().getMailAddressDao().insert(mail2);
        Configuration.getCurrent().getMailAddressDao().insert(FAVORIS_MAIL,USER_ID);
    }

    @AfterAll
    static public void closeDatabase() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void getAllName() throws SQLException {
        List<String> mailList = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertEquals(mail1,mailList.get(0));
        assertEquals(mail2,mailList.get(1));
    }

    @Test
    void getAllNameForUser() throws SQLException {
        List<String> favorisMail = Configuration.getCurrent().getMailAddressDao().getAllName(USER_ID);
        assertEquals(NUMBER_FAVORIS_MAIL,favorisMail.size());
        assertEquals(FAVORIS_MAIL,favorisMail.get(0));
    }

    @Test
    void insertForUser() throws SQLException {
        Configuration.getCurrent().getMailAddressDao().insert(FAVORIS_MAIL2,USER_ID);
        List<String> favorisMail = Configuration.getCurrent().getMailAddressDao().getAllName(USER_ID);
        assertEquals(NUMBER_FAVORIS_MAIL+1,favorisMail.size());
        assertEquals(FAVORIS_MAIL,favorisMail.get(0));
        assertEquals(FAVORIS_MAIL2,favorisMail.get(1));
    }

    @Test
    void insertForUserDuplicateAddress(){

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

    @Test
    void delete() throws SQLException {
        String mail4 = "mail9@ulb.com";
        Configuration.getCurrent().getMailAddressDao().insert(mail4);
        Configuration.getCurrent().getMailAddressDao().delete(mail4);
        List<String> allMailAddresses = Configuration.getCurrent().getMailAddressDao().getAllName();
        assertNotEquals(mail4,allMailAddresses.get(allMailAddresses.size()-1));

    }
}