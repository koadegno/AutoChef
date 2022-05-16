package ulb.infof307.g01.model.export;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;

/**
 * Test de la classe JSON
 */
class TestJSON {
    final static private String fileNameDB   = "testJSON.sqlite";
    final static private String fileNameRecipe = "testRecette.json";
    final static private String fileNameProduct = "testProduct.json";

    @BeforeAll
    static public void createDB() throws SQLException {
        Configuration.getCurrent().setDatabase(fileNameDB);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getRecipeCategoryDao().insert("Viande");
        Configuration.getCurrent().getRecipeTypeDao().insert("Desserts");

        Configuration.getCurrent().getProductFamilyDao().insert("soupes");
        Configuration.getCurrent().getProductUnityDao().insert("l");
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

        FileWriter fileJSON = new FileWriter(fileNameRecipe);
        fileJSON.write(recipe.toJSONString());
        fileJSON.flush();
    }

    @BeforeAll
     static public void createJSONProduct() throws IOException {
        JSONObject job = new JSONObject();
        JSONArray jab = new JSONArray();

        Map<String,String> m1 = new LinkedHashMap<>(4);
        m1.put("Unite", "l");
        m1.put("FamilleAliment", "soupes");
        m1.put("Nom", "miam2");
        jab.add(m1);

        m1 = new LinkedHashMap<>(4);
        m1.put("Unite", "l");
        m1.put("FamilleAliment", "soupes");
        m1.put("Nom", "miam");

        jab.add(m1);
        job.put("Liste",jab);

        FileWriter fileJSON = new FileWriter(fileNameProduct);
        fileJSON.write(job.toJSONString());
        fileJSON.flush();
        fileJSON.close();

    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("testJSON.sqlite"));
    }

    @Test
    public void testJsonRecipe() throws SQLException, IOException, ParseException {
        JSON json = new JSON();
        json.importRecipe(fileNameRecipe);
        //get recipe from database to check if it's created
    }

    @Test
    public void testJsonProduct() throws SQLException {
        JSON json = new JSON();
        json.importProduct(fileNameProduct);
        //get recipe from database to check if it's created
    }

}