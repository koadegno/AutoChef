package ulb.infof307.g01.db;
import ulb.infof307.g01.cuisine.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Database {

    private String dbName;
    private Connection connection;
    private Statement request;

    public Database(String nameDB) {
        dbName = "jdbc:sqlite:" + nameDB;
        File file = new File(nameDB);
        boolean fileExist = file.exists();
        try {
            connection = DriverManager.getConnection(dbName);
            request = connection.createStatement();
            if(! fileExist ) {
                createDB();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createDB(){
        try{
            FileInputStream fileDb =new FileInputStream("bdd.txt");
            Scanner scanner =new Scanner(fileDb);
            while(scanner.hasNextLine()){
                sendRequest(scanner.nextLine());
            }
            scanner.close();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Uniquement pour la creation de table
     * @param query requete sql
     * @return return True si la requete est effectuee, False sinon
     */
    public boolean sendRequest(String query) {
        try {
            return request.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Requete lecture de la database
     * @param query requete sql
     * @return resultat de la requete, ou null si la requete echoue
     */
    private ResultSet sendQuery(String query) {
        try {
            return request.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Requete ecriture de la database
     * @param query requete sql
     */
    private void sendQueryUpdate(String query) throws SQLException {
        request.executeUpdate(query);
    }

    /**
     * @return un id genere par la database, null si l'id n'est pas cree
     */
    private Integer getGeneratedID(){
        try {
            ResultSet getID = request.getGeneratedKeys();
            getID.next();
            return getID.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Les valeurs doivent etre encodees sous la forme : "'exemple'", pour les strings
     * Reste doit etre encode sous la forme : "exemple"
     * @param nameTable Le nom de la table dans laquelle inserer
     * @param values Les differentes valeurs a inserer dans l'ordre des colonnes
     */
    private void insert(String nameTable,String[] values) throws SQLException {
        StringBuilder req = new StringBuilder(String.format("INSERT INTO %s values (", nameTable));
        for (String value : values) {
            req.append(value).append(",");
        }
        req.deleteCharAt(req.length()-1);
        req.append(");");
        sendQueryUpdate(String.valueOf(req));
    }

    /**
     * @param nameTable La table pour laquel il faut faire un select
     * @param constraintToAppend liste de toute les contraintes à ajouter
     * @return Resultat de la query
     */
    private ResultSet select(String nameTable, List<String> constraintToAppend){

        StringBuilder req;
        String query;
        if (constraintToAppend.size() > 0){
            req = new StringBuilder(String.format("SELECT * FROM %s WHERE ", nameTable));
            query = appendValuesToWhere(req,constraintToAppend);
        } else {
            query = String.format("SELECT * FROM %s;", nameTable);
        }
        return sendQuery(query);
    }

    private String appendValuesToWhere(StringBuilder query, List<String> constraintToAppend) {

        for (String s : constraintToAppend) {
            query.append(s).append(" AND ");
        }
        query.delete(query.length()-4, query.length());
        query.append(";");
        return String.valueOf(query);
    }

    private void delete(String nameTable, List<String> constraintToAppend) throws SQLException {
        StringBuilder req = new StringBuilder(String.format("DELETE FROM %s WHERE ", nameTable));
        String stringQuery = appendValuesToWhere(req, constraintToAppend);
        sendQueryUpdate(stringQuery);
    }

    private void updateName(String nameTable, String nameToUpdate, List<String> constraintToAppend) throws SQLException {
        StringBuilder req = new StringBuilder(String.format("Update %s SET Nom = '%s' WHERE ", nameTable,nameToUpdate));
        String stringQuery = appendValuesToWhere(req, constraintToAppend);
        sendQueryUpdate(stringQuery);
    }

    private void insertIngredientInShoppingList(int courseId, int ingredientId, int quantity) throws SQLException {
        String[] values = {String.format("%d",courseId),String.format("%d",ingredientId),String.format("%d",quantity)};
        insert("ListeCourseIngredient",values);
    }

    /**
     * @param table Table dans laquelle on cherche le nom
     * @param id ID correspondant au nom
     * @param nameIDColumn nom de la colonne contenant l'ID
     * @return Nom correspondant à l'ID
     * @throws SQLException
     */
    private String getNameFromID(String table, int id, String nameIDColumn) throws SQLException {

        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s=%d",nameIDColumn,id));
        ResultSet res = select(table,constraint);
        res.next();
        return res.getString("Nom");
    }

    /**
     * Methode inverse de getNameFromID
     */
    private int getIDFromName(String table, String name, String nameIDColumn) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s='%s'","Nom",name));
        ResultSet res = select(table,constraint);
        res.next();
        return res.getInt(nameIDColumn);
    }

    /**
     * Methode remplissant une ArrayList a partir d'un objet ResultSet contenant des Recipes
     * @param result ResultSet qui contient le resultat de la requete
     * @return ArrayList d'objets Recipes correctement remplis
     * @throws SQLException
     */
    private ArrayList<Recipe> getRecipes(ResultSet result) throws SQLException {

        ArrayList<Recipe> recipes = new ArrayList<>();
        while (result.next()){
            int recipeID = result.getInt(1);
            String nom = result.getString(2);
            int duration = result.getInt(3);
            int nbPersons = result.getInt(4);
            String method = result.getString(5);
            String category = result.getString(6);
            String type = result.getString(7);
            Recipe recipe = new Recipe(recipeID, nom, duration, category, type, nbPersons, method);
            recipes.add(recipe);
        }
        return recipes;
    }

    /**
     * Insertion dans la table Categorie
     * @param nameCategory Nouvelle categorie à inserer
     */
    public void insertCategory(String nameCategory) throws SQLException {
        String[] val = {"null", String.format("'%s'", nameCategory)};
        insert("Categorie", val);
    }

    public void insertType(String nameType) throws SQLException {
        String[] val = {"null", String.format("'%s'", nameType)};
        insert("TypePlat", val);
    }

    public void insertMenu(String name, int duration) throws SQLException{
        String[] val = {"null",String.format("'%s'",name),String.format("'%d'",duration)};
        insert("Menu",val);
    }

    public void insertRecipe(Recipe recipe) throws SQLException {
        String name = String.format("'%s'", recipe.getName());
        String duration = String.format("%d", recipe.getDuration());
        String nbPerson = String.format("%d", recipe.getNbrPerson());
        String type = String.format("%d", getIDFromName("TypePlat", recipe.getType(), "TypePlatID") );
        String category = String.format("%d", getIDFromName("Categorie", recipe.getCategory(), "CategorieID") );
        String preparation = String.format("'%s'", recipe.getPreparation());

        String[] val = {"null", name, duration, nbPerson, type, category, preparation};
        insert("Recette", val);
    }

    public void insertUnite(String name) throws SQLException {
        String[] values = {"null",String.format("'%s'",name)};
        insert("Unite",values);
    }

    public void insertFamilleAliment(String name) throws SQLException {
        String[] values = {"null",String.format("'%s'",name)};
        insert("FamilleAliment",values);
    }

    public void insertIngredient(String name,String famille,String unite) throws SQLException {
        int idFamille = getIDFromName("FamilleAliment",famille,"FamilleAlimentID");
        int idUnite = getIDFromName("Unite",unite,"UniteID");
        String stringIdFamille = String.format("%d", idFamille);
        String stringIdUnite = String.format("%d",idUnite);
        String stringName = String.format("'%s'",name);
        String[] values = {"null",stringName, stringIdFamille,stringIdUnite};
        insert("Ingredient",values);
    }

    /**
     * @param idProduct ID de l'ingredient que l'on cherche
     * @return objet Product trouvé
     * @throws SQLException
     */
    private Product getProduct(int idProduct) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s = %d","IngredientID",idProduct));
        ResultSet querySelectProduct = select("Ingredient",constraint);
        querySelectProduct.next();
        String nameProduct = querySelectProduct.getString("Nom");
        String familleProduct = getNameFromID("FamilleAliment",querySelectProduct.getInt("FamilleAlimentID"),"FamilleAlimentID");
        String uniteProduct = getNameFromID("Unite",querySelectProduct.getInt("UniteID"),"UniteID");
        return new Product(nameProduct,1,uniteProduct,familleProduct);
    }

    private ArrayList<String> getAllNameFromTable(String table) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        ResultSet queryAllTableName = select(table, constraint);
        ArrayList<String> allProductName = new ArrayList<>();
        while(queryAllTableName.next()){
            allProductName.add(queryAllTableName.getString("Nom"));
        }
        return allProductName;
    }

    /**
     * @return ArrayList contenant le nom de toutes les categories
     * @throws SQLException
     */
    public ArrayList<String> getAllCategories() throws SQLException {
        return getAllNameFromTable("Categorie");
    }

    public ArrayList<String> getAllShoppingListName() throws SQLException {

        return getAllNameFromTable("ListeCourse");
    }

    public ArrayList<String> getAllProductName() throws SQLException {
        return getAllNameFromTable("Ingredient");
    }

    public ArrayList<String> getAllUniteName() throws SQLException {
        return getAllNameFromTable("Unite");
    }

    public ArrayList<String> getAllTypeName() throws SQLException {
        return getAllNameFromTable("TypePlat");
    }

    public ArrayList<String> getAllMenuName() throws SQLException {
        return getAllNameFromTable("Menu");
    }


    public ShoppingList getShoppingListFromName(String nameShoppingList) throws SQLException {

        int idName = getIDFromName("ListeCourse",nameShoppingList,"ListeCourseID");
        ResultSet querySelectShoppingList = sendQuery(String.format("SELECT S.Quantite,Ingredient.Nom,Unite.Nom,F.Nom\n" +
                "FROM ListeCourseIngredient as S\n" +
                "INNER JOIN Ingredient ON S.IngredientID = Ingredient.IngredientID\n" +
                "INNER JOIN Unite ON Ingredient.UniteID = Unite.UniteID \n" +
                "INNER JOIN FamilleAliment as F ON Ingredient.FamilleAlimentID = F.FamilleAlimentID\n" +
                "WHERE S.ListeCourseID = %d", idName));
        ShoppingList shoppingList = new ShoppingList(nameShoppingList,idName);
        while(querySelectShoppingList.next()){
            int productQuantity = querySelectShoppingList.getInt(1);
            String productName = querySelectShoppingList.getString(2);
            String productUnite = querySelectShoppingList.getString(3);
            String productFamille = querySelectShoppingList.getString(4);
            shoppingList.add(new Product(productName,productQuantity,productUnite,productFamille));
        }
        return shoppingList;
    }

    /**
     * Si un ou plusieurs parametres sont null ils ne sont pas ajoutes dans les contraintes de la requete
     * @param nameCategory contrainte pour le nom de la categorie
     * @param nameType contrainte pour le nom du type
     * @param nbPerson contrainte pour le nombre de personne
     * @return
     * @throws SQLException
     */
    public ArrayList<Recipe> getRecipeWhere(String nameCategory, String nameType, int nbPerson) throws SQLException {

        ArrayList<String> constraint = new ArrayList<>();
        String stringQuery;
        StringBuilder query = new StringBuilder("SELECT R.RecetteID, R.Nom, R.Duree, R.NbPersonnes, R.Preparation, Categorie.Nom, TypePlat.Nom\n" +
                "FROM Recette as R\n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n");

        if (nameCategory != null){
            int idCategory = getIDFromName("Categorie",nameCategory,"CategorieID");
            constraint.add(String.format("R.CategorieID = %d", idCategory));
        }
        if (nameType != null){
            int idType = getIDFromName("TypePlat",nameType,"TypePlatID");
            constraint.add(String.format("R.TypePlatID = %d", idType));
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
        return getRecipes(result);
    }

    public Integer createAndGetIdShoppingList(String name) throws SQLException {
        String[] values = {"null","'"+name+"'"};
        insert("ListeCourse",values);
        return getGeneratedID();
    }

    public void saveNewShoppingList(ShoppingList shoppingList) throws SQLException {
        int id = createAndGetIdShoppingList(shoppingList.getName());
        for(Product product : shoppingList){
            int idProduct = getIDFromName("Ingredient", product.getName(),"IngredientID");
            insertIngredientInShoppingList(id,idProduct, product.getQuantity());
       }
   }

   public void saveModifyShoppingList(ShoppingList shoppingList) throws SQLException {
       ArrayList<String> constraint = new ArrayList<>();
       constraint.add(String.format("%s = %d","ListeCourseID",shoppingList.getId()));
       delete("ListeCourseIngredient",constraint);
       updateName("ListeCourse", shoppingList.getName(),constraint );
       if(shoppingList.size() == 0){
           delete("ListeCourse",constraint);
       }
       else{
           for (Product product : shoppingList) {
               int idProduct = getIDFromName("Ingredient", product.getName(), "IngredientID");
               insertIngredientInShoppingList(shoppingList.getId(), idProduct, product.getQuantity());
           }

       }
   }

}