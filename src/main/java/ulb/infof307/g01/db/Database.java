package ulb.infof307.g01.db;
import ulb.infof307.g01.cuisine.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe représentant la Base de Données
 * Permet la création d'une nouvelle base de données ou d'en charger
 * une existante en donnant son nom en paramètre du constructeur
 */
public class Database {

    private Connection connection;
    private Statement request;

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public Database(String nameDB) {
        String dbName = "jdbc:sqlite:" + nameDB;
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

    /**
     * Méthode de création de la base de données qui lit et execute
     * ligne par ligne le fichier bdd.txt qui représente le ddl.
     */
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
     * Requete de la base de données uniquement pour la creation de table
     * @param query requete sql
     */
    public void sendRequest(String query) {
        try {
            request.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Requete d'accès en lecture de la base de données
     * @param query requete sql
     * @return resultat de la requete, ou null si la requete echoue
     */
    protected ResultSet sendQuery(String query) {
        try {
            return request.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Requete d'accès en écriture de la base de données
     * @param query requete sql
     */
    protected void sendQueryUpdate(String query) throws SQLException {
        request.executeUpdate(query);
    }

    /**
     * @return un id généré par la base de données, null si l'id n'est pas créé
     */
    protected Integer getGeneratedID(){
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
    protected void insert(String nameTable,String[] values) throws SQLException {
        StringBuilder query = new StringBuilder(String.format("INSERT INTO %s values (", nameTable));
        for (String value : values) {
            query.append(value).append(",");
        }
        query.deleteCharAt(query.length()-1);
        query.append(");");
        sendQueryUpdate(String.valueOf(query));
    }

    /**
     * @param nameTable La table pour laquel il faut faire un select
     * @param constraintToAppend liste de toute les contraintes à ajouter
     * @return Resultat de la query
     */
    protected ResultSet select(String nameTable, List<String> constraintToAppend,String orderBy){
        String stringQuery;
        if (constraintToAppend.size() > 0){
            StringBuilder query = new StringBuilder(String.format("SELECT * FROM %s WHERE ", nameTable));
            stringQuery = appendValuesToWhere(query,constraintToAppend);
        } else {
            stringQuery = String.format("SELECT * FROM %s ", nameTable);
        }
        if(orderBy != null){
            stringQuery += orderBy;
        }
        stringQuery += ";";
        return sendQuery(stringQuery);
    }

    protected void delete(String nameTable, List<String> constraintToAppend) throws SQLException {
        StringBuilder query = new StringBuilder(String.format("DELETE FROM %s WHERE ", nameTable));
        String stringQuery = appendValuesToWhere(query, constraintToAppend);
        sendQueryUpdate(stringQuery);
    }

    /**
     * Remplissage automatique d'une requête avec des contraintes
     * @param query requete à contraindre
     * @param constraintToAppend contraintes à ajouter à requête
     * @return requete avec contrainte
     */
    protected String appendValuesToWhere(StringBuilder query, List<String> constraintToAppend) {
        for (String s : constraintToAppend) {
            query.append(s).append(" AND ");
        }
        query.delete(query.length()-4, query.length());
        return String.valueOf(query);
    }

    protected void updateName(String nameTable, String nameToUpdate, List<String> constraintToAppend) throws SQLException {
        StringBuilder query = new StringBuilder(String.format("Update %s SET Nom = '%s' WHERE ", nameTable,nameToUpdate));
        String stringQuery = appendValuesToWhere(query, constraintToAppend);
        sendQueryUpdate(stringQuery);
    }

    protected void insertIngredientInShoppingList(int shoppingListId, int ingredientId, int quantity) throws SQLException {
        String[] values = {String.format("%d",shoppingListId),String.format("%d",ingredientId),String.format("%d",quantity)};
        insert("ListeCourseIngredient",values);
    }

    /**
     * @param table Table dans laquelle on cherche le nom
     * @param id ID correspondant au nom
     * @param nameIDColumn nom de la colonne contenant l'ID
     * @return Nom correspondant à l'ID
     */
    protected String getNameFromID(String table, int id, String nameIDColumn) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s=%d",nameIDColumn,id));
        ResultSet res = select(table,constraint,null);
        res.next();
        return res.getString("Nom");
    }

    /**
     * Methode inverse de getNameFromID
     */
    protected int getIDFromName(String table, String name, String nameIDColumn) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s='%s'","Nom",name));
        ResultSet res = select(table,constraint,null);
        res.next();
        return res.getInt(nameIDColumn);
    }

    /**
     * @param productID ID de l'ingredient que l'on cherche
     * @return objet Product trouvé
     */
    protected Product getProduct(int productID) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s = %d","IngredientID",productID));
        ResultSet querySelectProduct = select("Ingredient",constraint,null);
        querySelectProduct.next();
        String nameProduct = querySelectProduct.getString("Nom");
        String familyProduct = getNameFromID("FamilleAliment",querySelectProduct.getInt("FamilleAlimentID"),"FamilleAlimentID");
        String unityProduct = getNameFromID("Unite",querySelectProduct.getInt("UniteID"),"UniteID");
        return new Product(nameProduct,1,unityProduct,familyProduct);
    }

    /**
     *
     * @param orderBy si non nul, ajoute la contrainte de triée par
     */
    protected ArrayList<String> getAllNameFromTable(String table, String orderBy) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        ResultSet queryAllTableName = select(table, constraint,orderBy);
        ArrayList<String> allProductName = new ArrayList<>();
        while(queryAllTableName.next()){
            allProductName.add(queryAllTableName.getString("Nom"));
        }
        return allProductName;
    }

    protected void fillRecipeWithProducts(Recipe recipe) throws SQLException {
        ResultSet querySelectProduct = sendQuery(String.format("SELECT I.Nom, F.Nom, U.Nom, R.Quantite\n" +
                "FROM RecetteIngredient as R\n" +
                "INNER JOIN Ingredient as I ON R.IngredientID = I.IngredientID \n" +
                "INNER JOIN FamilleAliment as F ON F.FamilleAlimentID = I.FamilleAlimentID\n" +
                "INNER JOIN Unite as U ON U.UniteID = I.UniteID\n" +
                "WHERE R.RecetteID = %d", recipe.getId()));

        while(querySelectProduct.next()){
            String productName = querySelectProduct.getString(1);
            String familyName = querySelectProduct.getString(2);
            String unityName = querySelectProduct.getString(3);
            int quantity = querySelectProduct.getInt(4);

            Product product = new Product(productName, quantity, unityName,familyName);
            recipe.add(product);
        }
    }

    /**
     * Insertion dans la table Categorie
     * @param nameCategory Nouvelle categorie à inserer
     */
    public void insertCategory(String nameCategory) throws SQLException {
        String[] values = {"null", String.format("'%s'", nameCategory)};
        insert("Categorie", values);
    }

    public void insertType(String nameType) throws SQLException {
        String[] values = {"null", String.format("'%s'", nameType)};
        insert("TypePlat", values);
    }

    public void insertUnite(String name) throws SQLException {
        String[] values = {"null",String.format("'%s'",name)};
        insert("Unite",values);
    }

    public void insertFamilleAliment(String name) throws SQLException {
        String[] values = {"null",String.format("'%s'",name)};
        insert("FamilleAliment",values);
    }

    public void insertIngredient(Product product) throws SQLException {
        int familyID = getIDFromName("FamilleAliment",product.getFamillyProduct(),"FamilleAlimentID");
        int unityID = getIDFromName("Unite",product.getNameUnity(),"UniteID");
        String stringFamilyID = String.format("%d", familyID);
        String stringUnityID = String.format("%d",unityID);
        String stringName = String.format("'%s'",product.getName());
        String[] values = {"null",stringName, stringFamilyID,stringUnityID};
        insert("Ingredient",values);
    }

    /**
     * @return ArrayList contenant le nom de toutes les categories
     */
    public ArrayList<String> getAllCategories() throws SQLException {
        return getAllNameFromTable("Categorie",null);
    }

    public ArrayList<String> getAllShoppingListName() throws SQLException {
        return getAllNameFromTable("ListeCourse","ORDER BY Nom ASC");
    }

    public ArrayList<String> getAllProductName() throws SQLException {
        return getAllNameFromTable("Ingredient","ORDER BY Nom ASC");
    }

    public ArrayList<String> getAllUnityName() throws SQLException {
        return getAllNameFromTable("Unite",null);
    }

    public ArrayList<String> getAllTypeName() throws SQLException {
        return getAllNameFromTable("TypePlat",null);
    }


    public ShoppingList getShoppingListFromName(String nameShoppingList) throws SQLException {

        int nameID = getIDFromName("ListeCourse",nameShoppingList,"ListeCourseID");
        ResultSet querySelectShoppingList = sendQuery(String.format("SELECT S.Quantite,Ingredient.Nom,Unite.Nom,F.Nom\n" +
                "FROM ListeCourseIngredient as S\n" +
                "INNER JOIN Ingredient ON S.IngredientID = Ingredient.IngredientID\n" +
                "INNER JOIN Unite ON Ingredient.UniteID = Unite.UniteID \n" +
                "INNER JOIN FamilleAliment as F ON Ingredient.FamilleAlimentID = F.FamilleAlimentID\n" +
                "WHERE S.ListeCourseID = %d", nameID));
        ShoppingList shoppingList = new ShoppingList(nameShoppingList,nameID);
        while(querySelectShoppingList.next()){
            int productQuantity = querySelectShoppingList.getInt(1);
            String productName = querySelectShoppingList.getString(2);
            String productUnity = querySelectShoppingList.getString(3);
            String productFamily = querySelectShoppingList.getString(4);
            shoppingList.add(new Product(productName,productQuantity,productUnity,productFamily));
        }
        return shoppingList;
    }

    public Integer createAndGetIdShoppingList(String name) throws SQLException {
        String[] values = {"null",String.format("'%s'",name)};
        insert("ListeCourse",values);
        return getGeneratedID();
    }

    public void saveNewShoppingList(ShoppingList shoppingList) throws SQLException {
        int id = createAndGetIdShoppingList(shoppingList.getName());
        for(Product product : shoppingList){
            int productID = getIDFromName("Ingredient", product.getName(),"IngredientID");
            insertIngredientInShoppingList(id,productID, product.getQuantity());
        }
    }

    /**
     * Sauvegarde une liste de course modifiée, et la supprime si la liste ne contient rien
     */
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
               int productID = getIDFromName("Ingredient", product.getName(), "IngredientID");
               insertIngredientInShoppingList(shoppingList.getId(), productID, product.getQuantity());
           }

        }
    }
}