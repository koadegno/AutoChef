package ulb.infof307.g01.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ulb.infof307.g01.model.db.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class JSON
{
    public void jsonReader(String fileName)
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject recipe = (JSONObject) obj;

            String name = (String) recipe.get("Nom");
            Long duration = (Long) recipe.get("Duree");
            Long nbrPerson = (Long) recipe.get("NbPersonnes");
            String type = (String) recipe.get("TypePlat");
            String category = (String) recipe.get("Categorie");
            String preparation = (String) recipe.get("Preparation");

            //Envoyer recette à la base de donnee
            Recipe recipeToSend = new Recipe(name, Math.toIntExact(duration), category, type, Math.toIntExact(nbrPerson), preparation);
            try {
                Configuration.getCurrent().getRecipeDao().insert(recipeToSend);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
