package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.Database;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les recettes
 */
public class RecipeDao extends Database implements Dao<Recipe> {

    private static final String IS_NOT_FAVORITE = "0";
    private static final String RECIPE_TABLE_NAME = "Recette";
    private static final String RECIPE_PRODUCT_TABLE_NAME = "RecetteIngredient";
    private static final String RECIPE_USER_TABLE_NAME = "UtilisateurRecette";

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
        String preparations = result.getString(7);
        boolean isFavorite = result.getBoolean(8);
        return new Recipe.RecipeBuilder().withId(recipeID).withName(name).withDuration(duration).withCategory(category).withType(type).withNumberOfPerson(nbPersons).withPreparation(preparations).isFavorite(isFavorite).build();
    }

    /**
     * Methode remplissant une ArrayList a partir d'un objet ResultSet contenant des Recipes
     * @param result ResultSet qui contient le resultat de la requete
     * @return ArrayList d'objets Recipes correctement remplis
     */
    private List<Recipe> fillRecipes(ResultSet result) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        while (result.next()){
            Recipe recipe = fillRecipe(result);
            recipes.add(recipe);
        }
        for(Recipe products: recipes){
            fillRecipeWithProducts(products);
        }
        return recipes;
    }

    /**
     * Si un ou plusieurs parametres sont null ils ne sont pas ajoutes dans les contraintes de la requete
     * @param nameCategory contrainte pour le nom de la categorie
     * @param nameType contrainte pour le nom du type
     * @param nbPerson contrainte pour le nombre de personne
     * @return La liste des recettes qui correspondent au critère
     */
    public List<Recipe> getRecipeWhere(String nameCategory, String nameType, int nbPerson) throws SQLException {
        List<String> valuesOfPreparedStatement;
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("UtilisateurRecette.UtilisateurID = %d",
                Configuration.getCurrent().getCurrentUser().getId()));
        String stringQuery;
        StringBuilder query = new StringBuilder("""
                SELECT R.RecetteID,R.Nom,R.Duree,R.NbPersonnes,TypePlat.Nom,Categorie.Nom,R.Preparation,UtilisateurRecette.estFavoris
                FROM Recette as R
                INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID
                INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID
                INNER JOIN UtilisateurRecette ON R.RecetteID = UtilisateurRecette.RecetteID
                """);

        
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

        query.append(" Where ");
        valuesOfPreparedStatement = appendValuesToWherePreparedStatement(query,constraint);

        stringQuery = String.valueOf(query);
        PreparedStatement statement = connection.prepareStatement(stringQuery);
        fillPreparedStatementValues(statement, valuesOfPreparedStatement);
        ResultSet result = sendQuery(statement);
        return fillRecipes(result);
    }

    /**
     * Récupère tous les noms de recette
     * @return liste contenante tous les noms de recette, peut etre vide
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public List<String> getAllName() throws SQLException {
        String query = String.format("""
                SELECT R.Nom
                FROM Recette as R
                INNER JOIN UtilisateurRecette ON R.RecetteID = UtilisateurRecette.RecetteID
                WHERE UtilisateurRecette.UtilisateurID = %d
                ORDER BY Nom ASC
                """, getUserID());
        return getListOfName(query);
    }

    /**
     * insertUserMail la recette dans la basse de donnée
     * @param recipe la recette a insérer
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public void insert(Recipe recipe) throws SQLException {
        String isFavorite = String.format("%d", recipe.isFavorite() ? 1: 0);
        insertRecipe(recipe);
        String recipeID = String.format("%d", getGeneratedID());
        insertRecipeProducts(recipe, recipeID);
        insertUserRecipe(isFavorite, recipeID);
    }

    private void insertUserRecipe(String isFavorite, String recipeID) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (%s, %s, %s);
            """,RECIPE_USER_TABLE_NAME, getUserID() , recipeID, isFavorite);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            sendQueryUpdate(statement);
        }
    }

    private void insertRecipeProducts(Recipe recipe, String recipeID) throws SQLException {
        for (Product p: recipe) { // ajout des produits lié a la recette
            String productID = String.format("%d", getIDFromName("Ingredient", p.getName(), "IngredientID"));
            String quantity =  String.format("%d", p.getQuantity());
            String query = String.format("""
            INSERT INTO %s values (%s, %s, %s);
            """,RECIPE_PRODUCT_TABLE_NAME, recipeID,productID, quantity);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                sendQueryUpdate(statement);
            }
        }
    }

    private void insertRecipe(Recipe recipe) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (null,?,?,?,%s,%s,?);
            """,RECIPE_TABLE_NAME, getIDFromName("TypePlat", recipe.getType(), "TypePlatID"),getIDFromName("Categorie", recipe.getCategory(), "CategorieID") );
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recipe.getName());
            statement.setInt(2, recipe.getDuration());
            statement.setInt(3, recipe.getNbrPerson());
            statement.setString(4, recipe.getPreparation());
            sendQueryUpdate(statement);
        }
    }

    /**
     * supprime et ajoute la recette une nouvelle fois dans la base de donnée
     * @param recipe la recette a mettre a jour
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public void update(Recipe recipe) throws SQLException {
        delete(recipe);
        insert(recipe);
    }

    /**
     * Recupère les informations d'une recette
     * @param name le nom de la  recette
     * @return un objet Recette
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public Recipe get(String name) throws SQLException {
        String query = String.format("""
                SELECT R.RecetteID,R.Nom,R.Duree,R.NbPersonnes,TypePlat.Nom,Categorie.Nom,R.Preparation,UtilisateurRecette.estFavoris
                FROM Recette as R
                INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID
                INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID
                INNER JOIN UtilisateurRecette ON R.RecetteID = UtilisateurRecette.RecetteID
                WHERE R.Nom = ? and UtilisateurRecette.UtilisateurID = %d""", Configuration.getCurrent().getCurrentUser().getId());

        ArrayList<String> valuesOfPreparedStatement = new ArrayList<>();
        name = "'" + name + "'"; // quotes to recognize it as string and request need it
        valuesOfPreparedStatement.add(name);
        PreparedStatement statement = connection.prepareStatement(query);
        fillPreparedStatementValues(statement, valuesOfPreparedStatement);
        ResultSet result = sendQuery(statement);
        if(!result.next()) return null;

        Recipe recipe = fillRecipe(result);
        fillRecipeWithProducts(recipe);
        return recipe;
    }

    public void delete(Recipe displayedRecipe) throws SQLException {
        String[] constraint = {"RecetteID = "+ displayedRecipe.getId()};
        delete("UtilisateurRecette", List.of(constraint));
        delete("RecetteIngredient", List.of(constraint));
        delete("MenuRecette",List.of(constraint));
        delete("Recette",List.of(constraint));
    }

    public List<Recipe> getFavoriteRecipes() throws SQLException {
        List<Recipe>   recipes;
        List<Recipe>   favoriteRecipes = new ArrayList<>();
        recipes = getRecipeWhere(null, null, 0);
        for (Recipe recipe: recipes) {
            if(recipe.isFavorite()) favoriteRecipes.add(recipe);
        }

        return favoriteRecipes;
    }
}
