import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabase {

    @Test
    public void testExistenceFamilleAliment(){
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='{FamilleAliment}'";

    }

}