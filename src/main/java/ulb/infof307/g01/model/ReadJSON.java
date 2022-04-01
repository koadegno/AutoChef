package ulb.infof307.g01.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ulb.infof307.g01.db.Database;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadJSON
{
    @SuppressWarnings("unchecked")
    public static void main(String[] args)
    {
        Database db = new Database("autochef.sqlite");
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("testRecette.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject recette = (JSONObject) obj;
            System.out.println(recette);

            //Get recette Nom
            String Nom = (String) recette.get("Nom");
            System.out.println(Nom);

            //Get recette Duree
            Long Duree = (Long) recette.get("Duree");
            System.out.println(Duree);


            //Get recette NbPersonnes
            Long NbPersonnes = (Long) recette.get("NbPersonnes");
            System.out.println(NbPersonnes);

            //Get recette TypePlat
            String TypePlat = (String) recette.get("TypePlat");
            System.out.println(TypePlat);

            //Get recette Categorie
            String Categorie = (String) recette.get("Categorie");
            System.out.println(Categorie);

            //Get recette Preparation
            String Preparation = (String) recette.get("Preparation");
            System.out.println(Preparation);

            String insertRecettePart1 = "INSERT INTO Recette(Nom, Duree, NbPersonnes, TypePlatID, CategorieID, Preparation) VALUES (";
            String insertRecettePart2 = "( Select TypePlatID from TypePlat WHERE TypePlat.Nom = ";
            String insertRecettePart3 = "( Select CategorieID from Categorie WHERE Categorie.Nom = ";
            String queryRecette = insertRecettePart1 + "'"+ Nom + "', " +Duree+", "+ NbPersonnes +","+ insertRecettePart2 +"'"+ TypePlat+ "')," + insertRecettePart3+ "'"+ Categorie+"'),\""+Preparation+"\")";
            db.sendRequest(queryRecette);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
