package ulb.infof307.g01.model.export;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.exception.JSONException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * classe d'exportation d'une recette en JSON
 */
public class JSON {

    private String nameRecipeToAdd;
    private String nameProductToAdd;

    public void importProduct(String fileName) throws JSONException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject product = (JSONObject) obj;

            JSONArray liste = (JSONArray) product.get("Liste");
            Iterator i = liste.iterator();

            while(i.hasNext()){

                Object temp = i.next();
                JSONObject jsonObject2 = (JSONObject) temp;

                String unity = (String) jsonObject2.get("Unite");
                String familyAliment = (String) jsonObject2.get("FamilleAliment");
                String name = (String) jsonObject2.get("Nom");
                Product productToSend = new Product.ProductBuilder().withName(name).withFamilyProduct(familyAliment).withNameUnity(unity).build();
                //Envoyer recette à la base de donnee
                Configuration.getCurrent().getProductDao().insert(productToSend);
                nameProductToAdd = name;
            }
        } catch (IOException | ParseException | SQLException e) {
            throw new JSONException(e);
        }
    }
    public void importRecipe(String fileName) throws ParseException, IOException, SQLException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONObject recipe;
        try (FileReader reader = new FileReader(fileName)) {
            //Read JSON file
            recipe = (JSONObject) jsonParser.parse(reader);
        }
        nameRecipeToAdd = (String) recipe.get("Nom");
        Long duration = (Long) recipe.get("Duree");
        Long nbrPerson = (Long) recipe.get("NbPersonnes");
        String type = (String) recipe.get("TypePlat");
        String category = (String) recipe.get("Categorie");
        String preparation = (String) recipe.get("Preparation");
        Recipe recipeToSend = new Recipe.RecipeBuilder().withName(nameRecipeToAdd).withDuration(Math.toIntExact(duration)).withCategory(category).withType(type).withNumberOfPerson(Math.toIntExact(nbrPerson)).withPreparation(preparation).build();

        //Envoyer recette à la base de donnee
        Configuration.getCurrent().getRecipeDao().insert(recipeToSend);
    }
    public String getNameRecipe(){return this.nameRecipeToAdd;}
    public String getNameProduct(){return this.nameProductToAdd;}
}
