import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.export.JSON;


class TestJSON {
    final static private String fileNameDB   = "testJSON.sqlite";
    final static private String fileNameRecipe = "testRecette.json";
    final static private String fileNameProduct = "testProduct.json";

    @BeforeAll
    static public void createDB() throws SQLException {
        Configuration.getCurrent().setDatabase(fileNameDB);

        User testUser = new User("admin","admin",true);
        testUser.setID(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getRecipeCategoryDao().insert("Viande");
        Configuration.getCurrent().getRecipeTypeDao().insert("Desserts");

        Configuration.getCurrent().getProductFamilyDao().insert("soupes");
        Configuration.getCurrent().getProductUnityDao().insert("ml");
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

    @BeforeAll @SuppressWarnings("unchecked") //Warning pour le put car librairie pas a jour.
    static public void createJSONProduct() throws IOException {
        JSONObject product = new JSONObject();
        product.put("Nom", "testProduit");
        product.put("FamilleAliment", "soupes");
        product.put("Unite", "ml");

        FileWriter fileJSON = new FileWriter(fileNameProduct);
        fileJSON.write(product.toJSONString());
        fileJSON.flush();
    }

    @AfterAll
    static public void deleteDB() throws IOException, SQLException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("testJSON.sqlite"));
    }

    @Test
    public void testJsonRecipe(){
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