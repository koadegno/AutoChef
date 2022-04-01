package ulb.infof307.g01.db;

import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeDao extends Database implements Dao<Recipe> {

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public RecipeDao(String nameDB) {
        super(nameDB);
    }

    private Recipe fillRecipe(ResultSet result) throws SQLException {
        int recipeID = result.getInt(1);
        String name = result.getString(2);
        int duration = result.getInt(3);
        int nbPersons = result.getInt(4);
        String type = result.getString(5);
        String category = result.getString(6);
        String method = result.getString(7);
        return new Recipe(recipeID, name, duration, category, type, nbPersons, method);
    }

    /**
     * Methode remplissant une ArrayList a partir d'un objet ResultSet contenant des Recipes
     * @param result ResultSet qui contient le resultat de la requete
     * @return ArrayList d'objets Recipes correctement remplis
     */
    private ArrayList<Recipe> fillRecipes(ResultSet result) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (result.next()){
            Recipe recipe = fillRecipe(result);
            recipes.add(recipe);
        }
        for(Recipe rec: recipes){
            fillRecipeWithProducts(rec);
        }
        return recipes;
    }

    /**
     * Si un ou plusieurs parametres sont null ils ne sont pas ajoutes dans les contraintes de la requete
     * @param nameCategory contrainte pour le nom de la categorie
     * @param nameType contrainte pour le nom du type
     * @param nbPerson contrainte pour le nombre de personne
     */
    public ArrayList<Recipe> getRecipeWhere(String nameCategory, String nameType, int nbPerson) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        String stringQuery;
        StringBuilder query = new StringBuilder("SELECT R.RecetteID,R.Nom,R.Duree,R.NbPersonnes,TypePlat.Nom,Categorie.Nom,R.Preparation\n" +
                "FROM Recette as R\n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n");

        if (nameCategory != null){
            int categoryID = getIDFromName("Categorie",nameCategory,"CategorieID");
            constraint.add(String.format("R.CategorieID = %d", categoryID));
        }
        if (nameType != null){
            int typeID = getIDFromName("TypePlat",nameType,"TypePlatID");
            constraint.add(String.format("R.TypePlatID = %d", typeID));
        }
        if (nbPerson > 0){
            constraint.add(String.format("R.NbPersonnes = %d", nbPerson));
        }
        if (constraint.size() > 0){
            query.append(" Where ");
            stringQuery = appendValuesToWhere(query,constraint);
        }
        else {
            stringQuery = String.valueOf(query);
        }
        ResultSet result = sendQuery(stringQuery);
        return fillRecipes(result);
    }

    @Override
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable("Recette","ORDER BY Nom ASC");
    }

    @Override
    public void insert(Recipe recipe) throws SQLException {
        String name = String.format("'%s'", recipe.getName());
        String duration = String.format("%d", recipe.getDuration());
        String nbPerson = String.format("%d", recipe.getNbrPerson());
        String type = String.format("%d", getIDFromName("TypePlat", recipe.getType(), "TypePlatID") );
        String category = String.format("%d", getIDFromName("Categorie", recipe.getCategory(), "CategorieID") );
        String preparation = String.format("'%s'", recipe.getPreparation());

        String[] values = {"null", name, duration, nbPerson, type, category, preparation};
        insert("Recette", values);
        String recipeID = String.format("%d", getGeneratedID());

        for (Product p: recipe) {
            String productID = String.format("%d", getIDFromName("Ingredient", p.getName(), "IngredientID"));
            String quantity =  String.format("%d", p.getQuantity());
            String[] productValues = {recipeID, productID, quantity};
            insert("RecetteIngredient", productValues);
        }
    }

    @Override
    public void update(Recipe products) throws SQLException {
        System.out.println("A FAIRE ENCORE");
    }

    @Override
    public Recipe get(String name) throws SQLException {
        StringBuilder query = new StringBuilder(String.format("SELECT R.RecetteID,R.Nom,R.Duree,R.NbPersonnes,TypePlat.Nom,Categorie.Nom,R.Preparation\n" +
                "FROM Recette as R\n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n" +
                "WHERE R.Nom = '%s'", name));
        ResultSet result = sendQuery(query.toString());
        result.next();

        Recipe recipe = fillRecipe(result);
        fillRecipeWithProducts(recipe);
        return recipe;
    }
}
