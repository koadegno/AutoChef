package ulb.infof307.g01.model.export;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.exception.JSONException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test de la classe JSON
 */
class TestJSON {
    final static private String fileNameDB   = "testJSON.sqlite";
    final static private String fileNameRecipe = "testRecette.json";
    final static private String fileNameProduct = "testProduct.json";
    final static private Configuration configuration = Configuration.getCurrent();


    @BeforeAll
    static public void createDB() throws SQLException {
        configuration.setDatabase(fileNameDB);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        configuration.setCurrentUser(testUser);

        configuration.getRecipeCategoryDao().insert("Viande");
        configuration.getRecipeTypeDao().insert("Desserts");

        configuration.getProductFamilyDao().insert("soupes");
        configuration.getProductUnityDao().insert("l");
    }

    @BeforeAll @SuppressWarnings("unchecked") //Warning pour le put car librairie pas a jour.
    static public void createJSONRecipe() throws IOException {
        JSONObject recipe = new JSONObject();
        recipe.put("Nom", "testRecette");
        recipe.put("Duree", 11);
        recipe.put("NbPersonnes", 2);
        recipe.put("TypePlat", "Desserts");
        recipe.put("Categorie", "Viande");
        recipe.put("Preparation", "mix then eat");

        try (FileWriter fileJSON = new FileWriter(fileNameRecipe)) {
            fileJSON.write(recipe.toJSONString());
            fileJSON.flush();
        }
    }

    @BeforeAll
     static public void createJSONProduct() throws IOException {
        JSONObject job = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        Map<String,String> linkedHashMap = new LinkedHashMap<>(4);
        linkedHashMap.put("Unite", "l");
        linkedHashMap.put("FamilleAliment", "soupes");
        linkedHashMap.put("Nom", "miam2");
        jsonArray.add(linkedHashMap);

        linkedHashMap = new LinkedHashMap<>(4);
        linkedHashMap.put("Unite", "l");
        linkedHashMap.put("FamilleAliment", "soupes");
        linkedHashMap.put("Nom", "miam");

        jsonArray.add(linkedHashMap);
        job.put("Liste",jsonArray);

        try (FileWriter fileJSON = new FileWriter(fileNameProduct)) {
            fileJSON.write(job.toJSONString());
            fileJSON.flush();
        }

    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of("testJSON.sqlite"));
    }

    @Test
    public void testJsonRecipe() throws SQLException, IOException, ParseException {
        JSON json = new JSON();
        json.importRecipe(fileNameRecipe);
        //get recipe from database to check if it's created
    }

    @Test
    public void testJsonProduct() throws JSONException {
        JSON json = new JSON();
        json.importProduct(fileNameProduct);
        //get recipe from database to check if it's created
    }
}