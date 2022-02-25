import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabase {
    private static Database db;

    @BeforeAll
    static public void creationDB(){
        db = new Database("test.sqlite");
    }

    @AfterAll
    static public void suppressionDB() throws IOException, SQLException {
        db.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    public void testCreationFamilleAliment(){
        db.creationTableFamilleAliment();
        String query = "SELECT Nom FROM FamilleAliment;";
        Boolean res = db.sendRequest(query);
        assertEquals(res, true);
    }

    @Test
    public void testNotCreateSameTableTwice(){
        boolean result = db.creationTableFamilleAliment();
        assertEquals(result, false);
    }

}