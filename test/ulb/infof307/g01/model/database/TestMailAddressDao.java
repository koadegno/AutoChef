package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestMailAddressDao {
    static private final String mail1 = "mail1@mail.be";
    static private final String mail2 = "mail2@ulb.ac.be";
    static private final String mail3 = "mail3@google.be";
    static private final String databaseName = "test.sqlite";


    @BeforeAll
    static public void setUp() throws SQLException {
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getMailAddressDao().insert(mail1);
        Configuration.getCurrent().getMailAddressDao().insert(mail2);
        
    }

    @AfterAll
    static public void tearDown() throws SQLException, IOException {
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
    void insert() throws SQLException {
        Configuration.getCurrent().getMailAddressDao().insert(mail3);
        String mailInserted = Configuration.getCurrent().getMailAddressDao().get(mail3);
        assertEquals(mail3,mailInserted);
    }
    
    @Test
    void get() throws SQLException {
        String mailGot = Configuration.getCurrent().getMailAddressDao().get(mail1);
        assertEquals(mail1,mailGot);
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