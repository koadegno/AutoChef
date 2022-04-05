package ulb.infof307.g01.db;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ulb.infof307.g01.db.Database;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSON
{
    public void jsonReader(String fileName)
    {
        Database db = new Database("autochef.sqlite");
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject recette = (JSONObject) obj;

            String Nom = (String) recette.get("Nom");
            Long Duree = (Long) recette.get("Duree");
            Long NbPersonnes = (Long) recette.get("NbPersonnes");
            String TypePlat = (String) recette.get("TypePlat");
            String Categorie = (String) recette.get("Categorie");
            String Preparation = (String) recette.get("Preparation");

            String insertRecettePart1 = "INSERT INTO Recette(Nom, Duree, NbPersonnes, TypePlatID, CategorieID, Preparation) VALUES (";
            String insertRecettePart2 = "( Select TypePlatID from TypePlat WHERE TypePlat.Nom = ";
            String insertRecettePart3 = "( Select CategorieID from Categorie WHERE Categorie.Nom = ";
            String queryRecette = insertRecettePart1 + "'"+ Nom + "', " +Duree+", "+ NbPersonnes +","+ insertRecettePart2 +"'"+ TypePlat+ "')," + insertRecettePart3+ "'"+ Categorie+"'),\""+Preparation+"\")";
            db.sendRequest(queryRecette);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
