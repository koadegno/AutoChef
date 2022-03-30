package ulb.infof307.g01.db;
import ulb.infof307.g01.model.*;

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
    private ResultSet sendQuery(String query) {
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
    private void sendQueryUpdate(String query) throws SQLException {
        request.executeUpdate(query);
    }

    /**
     * @return un id généré par la base de données, null si l'id n'est pas créé
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
    private ResultSet select(String nameTable, List<String> constraintToAppend,String orderBy){
        StringBuilder req;
        String query;
        if (constraintToAppend.size() > 0){
            req = new StringBuilder(String.format("SELECT * FROM %s WHERE ", nameTable));
            query = appendValuesToWhere(req,constraintToAppend);
        } else {
            query = String.format("SELECT * FROM %s ", nameTable);
        }
        if(orderBy != null){
            query += orderBy;
        }
        query += ";";
        return sendQuery(query);
    }

    private void delete(String nameTable, List<String> constraintToAppend) throws SQLException {
        StringBuilder req = new StringBuilder(String.format("DELETE FROM %s WHERE ", nameTable));
        String stringQuery = appendValuesToWhere(req, constraintToAppend);
        sendQueryUpdate(stringQuery);
    }

    /**
     * Remplissage automatique d'une requête avec des contraintes
     * @param query requete à contraindre
     * @param constraintToAppend contraintes à ajouter à requête
     * @return requete avec contrainte
     */
    private String appendValuesToWhere(StringBuilder query, List<String> constraintToAppend) {
        for (String s : constraintToAppend) {
            query.append(s).append(" AND ");
        }
        query.delete(query.length()-4, query.length());
        return String.valueOf(query);
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
     */
    private String getNameFromID(String table, int id, String nameIDColumn) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s=%d",nameIDColumn,id));
        ResultSet res = select(table,constraint,null);
        res.next();
        return res.getString("Nom");
    }

    /**
     * Methode inverse de getNameFromID
     */
    private int getIDFromName(String table, String name, String nameIDColumn) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s='%s'","Nom",name));
        ResultSet res = select(table,constraint,null);
        res.next();
        return res.getInt(nameIDColumn);
    }

    /**
     * Methode remplissant une ArrayList a partir d'un objet ResultSet contenant des Recipes
     * @param result ResultSet qui contient le resultat de la requete
     * @return ArrayList d'objets Recipes correctement remplis
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
        for(Recipe rec: recipes){
            fillRecipeWithProducts(rec);
        }
        return recipes;
    }

    /**
     * @param idProduct ID de l'ingredient que l'on cherche
     * @return objet Product trouvé
     */
    private Product getProduct(int idProduct) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s = %d","IngredientID",idProduct));
        ResultSet querySelectProduct = select("Ingredient",constraint,null);
        querySelectProduct.next();
        String nameProduct = querySelectProduct.getString("Nom");
        String familleProduct = getNameFromID("FamilleAliment",querySelectProduct.getInt("FamilleAlimentID"),"FamilleAlimentID");
        String uniteProduct = getNameFromID("Unite",querySelectProduct.getInt("UniteID"),"UniteID");
        return new Product(nameProduct,1,uniteProduct,familleProduct);
    }

    /**
     *
     * @param orderBy si non nul, ajoute la contrainte de triée par
     */
    private ArrayList<String> getAllNameFromTable(String table,String orderBy) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        ResultSet queryAllTableName = select(table, constraint,orderBy);
        ArrayList<String> allProductName = new ArrayList<>();
        while(queryAllTableName.next()){
            allProductName.add(queryAllTableName.getString("Nom"));
        }
        return allProductName;
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

    public void insertRecipe(Recipe recipe) throws SQLException {
        String name = String.format("'%s'", recipe.getName());
        String duration = String.format("%d", recipe.getDuration());
        String nbPerson = String.format("%d", recipe.getNbrPerson());
        String type = String.format("%d", getIDFromName("TypePlat", recipe.getType(), "TypePlatID") );
        String category = String.format("%d", getIDFromName("Categorie", recipe.getCategory(), "CategorieID") );
        String preparation = String.format("'%s'", recipe.getPreparation());

        String[] val = {"null", name, duration, nbPerson, type, category, preparation};
        insert("Recette", val);
        String recipeID = String.format("%d", getGeneratedID());

        for (Product p: recipe) {
            String productID = String.format("%d", getIDFromName("Ingredient", p.getName(), "IngredientID"));
            String quantity =  String.format("%d", p.getQuantity());
            String[] productVal = {recipeID, productID, quantity};
            insert("RecetteIngredient", productVal);
        }
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
        insertIngredient(product.getName(), product.getFamillyProduct(), product.getNameUnity());
    }

    private void insertIngredient(String name,String famille,String unite) throws SQLException {
        int idFamille = getIDFromName("FamilleAliment",famille,"FamilleAlimentID");
        int idUnite = getIDFromName("Unite",unite,"UniteID");
        String stringIdFamille = String.format("%d", idFamille);
        String stringIdUnite = String.format("%d",idUnite);
        String stringName = String.format("'%s'",name);
        String[] values = {"null",stringName, stringIdFamille,stringIdUnite};
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

    public ArrayList<String> getAllUniteName() throws SQLException {
        return getAllNameFromTable("Unite",null);
    }

    public ArrayList<String> getAllTypes() throws SQLException {
        return getAllNameFromTable("TypePlat",null);
    }

    public ArrayList<String> getAllMenuName() throws SQLException {
        return getAllNameFromTable("Menu","ORDER BY Nom ASC");
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
        String[] values = {"null",String.format("'%s'",name)};
        insert("ListeCourse",values);
        return getGeneratedID();
    }

    public Integer createAndGetIdMenu(String name, int duration) throws SQLException{
        String[] val = {"null",String.format("'%s'",name),String.format("%d",duration)};
        insert("Menu",val);
        return getGeneratedID();
    }

    public void saveNewShoppingList(ShoppingList shoppingList) throws SQLException {
        int id = createAndGetIdShoppingList(shoppingList.getName());
        for(Product product : shoppingList){
            int idProduct = getIDFromName("Ingredient", product.getName(),"IngredientID");
            insertIngredientInShoppingList(id,idProduct, product.getQuantity());
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
               int idProduct = getIDFromName("Ingredient", product.getName(), "IngredientID");
               insertIngredientInShoppingList(shoppingList.getId(), idProduct, product.getQuantity());
           }

        }
    }

    private void fillRecipeWithProducts(Recipe recipe) throws SQLException {
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

    private Menu fillMenuWithRecipes(String menuName) throws SQLException {
        int idName = getIDFromName("Menu",menuName,"MenuID");
        ResultSet querySelectMenu = sendQuery(String.format("SELECT M.Jour,M.Heure,R.RecetteID, R.Nom, R.Duree," +
                " R.NbPersonnes, R.Preparation, Categorie.Nom, TypePlat.Nom\n" +
                "FROM MenuRecette as M\n" +
                "INNER JOIN Recette as R ON M.RecetteID = R.RecetteID \n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n" +
                "WHERE M.MenuID = %d", idName));
        Menu menu = new Menu(menuName);
        while(querySelectMenu.next()){
           int menuDay = querySelectMenu.getInt(1);
           int menuHour = querySelectMenu.getInt(2);
           int recetteID = querySelectMenu.getInt(3);
           String recetteName = querySelectMenu.getString(4);
           int recetteDuration = querySelectMenu.getInt(5);
           int recetteNumberPersons = querySelectMenu.getInt(6);
           String recettePreparation = querySelectMenu.getString(7);
           String categoryName = querySelectMenu.getString(8);
           String typeName = querySelectMenu.getString(9);
           Recipe recipe = new Recipe(recetteID,recetteName,recetteDuration,categoryName,typeName,recetteNumberPersons,recettePreparation);
           menu.addRecipeToIndex(menuDay,menuHour,recipe);
        }
        return menu;
    }

    public Menu getMenuFromName(String menuName) throws SQLException {
        Menu menu = fillMenuWithRecipes(menuName);
        for(Day day : Day.values()){
            List<Recipe> recipesFromMenu = menu.getRecipesfor(day);
            for(Recipe recipe : recipesFromMenu){
                fillRecipeWithProducts(recipe);
            }
        }
        return menu;
    }

    private void insertRecipeInMenu(int menuID, int day, int hour, int recipeID) throws SQLException {
        String[] values = {String.format("%d",menuID),String.format("%d",day),String.format("%d",hour),String.format("%d",recipeID)};
        insert("MenuRecette",values);
    }

    public void saveNewMenu(Menu menu) throws  SQLException{
        int id = createAndGetIdMenu(menu.getName(),menu.getNbOfdays());
        createMenuRecipe(menu, id);
    }

    /**
     * Sauvegarde un menu modifiée, et le supprime si il ne contient rien
     */
    public void saveModifyMenu(Menu menu) throws  SQLException{
        ArrayList<String> constraint = new ArrayList<>();
        int menuID = getIDFromName("Menu",menu.getName(),"MenuID");
        constraint.add(String.format("%s = %d","MenuID",menuID));
        delete("MenuRecette",constraint);
        updateName("Menu", menu.getName(),constraint);
        if(menu.size() == 0){
            delete("Menu",constraint);
        }
        else {
            createMenuRecipe(menu, menuID);
        }
    }

    private void createMenuRecipe(Menu menu, int menuID) throws SQLException {
        for(Day day : Day.values()){
            List<Recipe> recipeOfDay = menu.getRecipesfor(day);
            for (int hour = 0; hour < recipeOfDay.size(); hour++) {
                int idRecipe = getIDFromName("Recette", recipeOfDay.get(hour).getName(), "RecetteID");
                insertRecipeInMenu(menuID, day.getIndex(), hour, idRecipe);
            }
        }
    }

}