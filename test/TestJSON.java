import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.model.JSON;


class TestJSON {
    final static private String fileNameDB   = "testJSON.sqlite";
    final static private String fileNameJSON = "testRecette.json";

    @BeforeAll
    static public void createDB() throws SQLException {
        Configuration.getCurrent().setDatabase(fileNameDB);

        Configuration.getCurrent().getRecipeCategoryDao().insert("Viande");
        Configuration.getCurrent().getRecipeTypeDao().insert("Desserts");
    }

    @BeforeAll @SuppressWarnings("unchecked") //Warning pour le put car librairie pas a jour.
    static public void createJSON() throws IOException {
        JSONObject recipe = new JSONObject();
        recipe.put("Nom", "testRecette");
        recipe.put("Duree", 11);
        recipe.put("NbPersonnes", 2);
        recipe.put("TypePlat", "Desserts");
        recipe.put("Categorie", "Viande");
        recipe.put("Preparation", "mix then eat");

        FileWriter fileJSON = new FileWriter(fileNameJSON);
        fileJSON.write(recipe.toJSONString());
        fileJSON.flush();
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("testJSON.sqlite"));
    }

    @Test
    public void testJsonReader(){
        JSON json = new JSON();
        json.jsonReader(fileNameJSON);
        //get recipe from database to check if it's created
    }



}