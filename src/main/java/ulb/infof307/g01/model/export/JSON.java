package ulb.infof307.g01.model.export;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * classe d'exportation d'une recette en JSON
 */
public class JSON {

    private String nameRecipeToAdd;
    private String nameProductToAdd;

    public void importProduct(String fileName) throws SQLException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject product = (JSONObject) obj;

            nameProductToAdd = (String) product.get("Nom");
            String familleAliment = (String) product.get("FamilleAliment");
            String unite = (String) product.get("Unite");

            //Envoyer recette à la base de donnee
            Product productToSend = new Product.ProductBuilder().withName(nameProductToAdd).withFamilyProduct(familleAliment).withNameUnity(unite).build();
            Configuration.getCurrent().getProductDao().insert(productToSend);


        } catch (IOException | ParseException e) {
            throw new SQLException(e);
        }

    }
    public void importRecipe(String fileName) throws SQLException, ParseException, IOException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        JSONObject recipe;
        try (FileReader reader = new FileReader(fileName)) {
            //Read JSON file
            recipe = (JSONObject) jsonParser.parse(reader);
        } catch (IOException e) {
            throw new IOException(e);
        }

        try{
            nameRecipeToAdd = (String) recipe.get("Nom");
            Long duration = (Long) recipe.get("Duree");
            Long nbrPerson = (Long) recipe.get("NbPersonnes");
            String type = (String) recipe.get("TypePlat");
            String category = (String) recipe.get("Categorie");
            String preparation = (String) recipe.get("Preparation");

            //Envoyer recette à la base de donnee

            Recipe recipeToSend = new Recipe.RecipeBuilder().withName(nameRecipeToAdd).withDuration(Math.toIntExact(duration)).withCategory(category).withType(type).withNumberOfPerson(Math.toIntExact(nbrPerson)).withPreparation(preparation).build();
            Configuration.getCurrent().getRecipeDao().insert(recipeToSend);

        }
        catch (Exception e){
            throw new ParseException(0, e);
        }





    }
    public String getNameRecipe(){return this.nameRecipeToAdd;}
    public String getNameProduct(){return this.nameProductToAdd;}
}
