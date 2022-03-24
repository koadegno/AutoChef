package ulb.infof307.g01.cuisine;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

public class WriteJSON
{
    @SuppressWarnings("unchecked")
    public static void main( String[] args )
    {
        //First Employee
        JSONObject recette = new JSONObject();
        recette.put("Nom", "testRecette");
        recette.put("Duree", 11);
        recette.put("NbPersonnes", 2);
        recette.put("TypePlat", "Desserts");
        recette.put("Categorie", "Viande");
        recette.put("Preparation", "mix then eat");


        //Write JSON file
        try (FileWriter file = new FileWriter("testRecette.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(recette.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
