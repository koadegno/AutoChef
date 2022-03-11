package ulb.infof307.g01.db;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.cuisine.ShoppingList;

import java.sql.*;
import java.util.ArrayList;


public class Database {
    private String dbName;
    private Connection connection;
    private Statement request;

    public Database(String nameDB) {
        dbName = "jdbc:sqlite:" + nameDB;
        try {
            connection = DriverManager.getConnection(dbName);
            request = connection.createStatement();
            createDB();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createDB(){
        createTableFamilleAliment();
        createTableUnite();
        createTableCategorie();
        createTableTypePlat();
        createTableRecette();
        createTableIngredient();
        createTableRecetteIngredient();
        createTableListeCourse();
        createTableListeCourseIngredient();
        createTableMenuRecette();
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    // TODO : METTRE EN PRIVATE
    public boolean sendRequest(String req) {
        try {
            return request.execute(req);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ResultSet sendQuery(String query) {
        try {
            return request.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendQueryUpdate(String query){
        try {
             request.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean createTableFamilleAliment() {
        String req = "CREATE TABLE IF NOT EXISTS FamilleAliment (\n" +
                "    FamilleAlimentID INTEGER PRIMARY KEY,\n" +
                "    Nom CHAR(20) NOT NULL);";
        return sendRequest(req);
    }

    private boolean createTableUnite() {
        String req = "CREATE TABLE IF NOT EXISTS Unite(\n" +
                "    UniteID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(10) NOT NULL UNIQUE\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableCategorie() {
        String req = "CREATE TABLE IF NOT EXISTS Categorie(\n" +
                "    CategorieID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(20) NOT NULL UNIQUE\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableTypePlat() {
        String req = "CREATE TABLE IF NOT EXISTS TypePlat(\n" +
                "    TypePlatID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(20) NOT NULL UNIQUE\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableRecette() {
        String req = "CREATE TABLE IF NOT EXISTS Recette(\n" +
                "    RecetteID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(40) NOT NULL,\n" +
                "    Duree INTEGER NOT NULL,\n" +
                "    NbPersonnes INTEGER NOT NULL,\n" +
                "    TypePlatID INTEGER NOT NULL\n" +
                "        CONSTRAINT fkTypePlat REFERENCES TypePlat(TypePlatID),\n" +
                "    CategorieID INTEGER NOT NULL\n" +
                "        CONSTRAINT fkCategorie REFERENCES Categorie(CategorieID),\n" +
                "    Preparation TEXT NOT NULL\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableIngredient() {
        String req = "CREATE TABLE IF NOT EXISTS Ingredient(\n" +
                "    IngredientID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(25) NOT NULL UNIQUE,\n" +
                "    FamilleAlimentID INTEGER\n" +
                "        CONSTRAINT fkFamilleAliment REFERENCES FamilleAliment(FamilleAlimentID),\n" +
                "    UniteID INTEGER\n" +
                "        CONSTRAINT fkUnite REFERENCES Unite(UniteID)\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableRecetteIngredient() {
        String req = "CREATE TABLE IF NOT EXISTS RecetteIngredient(\n" +
                "    RecetteID INTEGER NOT NULL\n" +
                "        CONSTRAINT fkRecette REFERENCES Recette(RecetteID),\n" +
                "    IngredientID INTEGER NOT NULL\n" +
                "        CONSTRAINT fkIngredient REFERENCES Ingredient(IngredientID),\n" +
                "    Quantite INTEGER NOT NULL,\n" +
                "    PRIMARY KEY (RecetteID, IngredientID)\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableListeCourse() {
        String req = "CREATE TABLE IF NOT EXISTS ListeCourse (\n" +
                "    ListeCourseID INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                "    Nom CHAR(25) NOT NULL UNIQUE \n" +
                ");";
        return sendRequest(req);
    }

    private boolean createTableListeCourseIngredient() {
        String req = "CREATE TABLE IF NOT EXISTS ListeCourseIngredient(\n" +
                "    ListeCourseID INTEGER\n" +
                "        CONSTRAINT fkListeCourse REFERENCES ListeCourse(ListeCourseID),\n" +
                "    IngredientID INTEGER\n" +
                "        CONSTRAINT fkIngredient REFERENCES Ingredient(IngredientID),\n" +
                "    Quantite INTEGER NOT NULL,\n" +
                "    PRIMARY KEY (ListeCourseID, IngredientID)\n" +
                ")";
        return sendRequest(req);
    }

    private boolean createTableMenuRecette() {
        String req = "CREATE TABLE IF NOT EXISTS MenuRecette(\n" +
                "    Date DATE NOT NULL,\n" +
                "    Heure TIME,\n" +
                "    RecetteID INTEGER\n" +
                "        CONSTRAINT fkRecette REFERENCES Recette(RecetteID),\n" +
                "    PRIMARY KEY (Date, Heure)\n" +
                ")";
        return sendRequest(req);
    }

    private Integer getkey(){
        try {
            ResultSet getKey = request.getGeneratedKeys();
            getKey.next();
            return getKey.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Les valeurs doivent etre encodees sous la forme pour les strings : "'exemple'"
     * Reste doit etre encode sous la forme : "exemple"
     * @param nameTable Le nom de la table dans laquelle inserer
     * @param values Les differentes valeurs a inserer dans l'ordre des colonnes
     */
    private void insert(String nameTable,String[] values){
        StringBuilder req = new StringBuilder(String.format("INSERT INTO %s values (", nameTable));
        for (String value : values) {
            req.append(value).append(",");
        }
        req.deleteCharAt(req.length()-1);
        req.append(");");
        sendQueryUpdate(String.valueOf(req));
    }

    /**
     * @param nameTable La table pour laquelel il faut faire un select
     * @param names Nom des colonnes qui seront ajoutees dans les contraintes
     * @param signs Signe associé a une contrainte (ex : =, <, > ...)
     * @param values Valeur a respecter dans la contrainte
     * @return Resultat de la query
     */
    private ResultSet select(String nameTable,String[]names, String[]signs, String[]values){
        StringBuilder req;
        if (values.length > 0){
            req = new StringBuilder(String.format("SELECT * FROM %s WHERE ", nameTable));
            for (int i = 0; i < names.length;i++) {
                req.append(names[i]).append(signs[i]).append(values[i]).append("AND");
            }
            req.delete(req.length()-3, req.length());
        } else {
            req = new StringBuilder(String.format("SELECT * FROM %s", nameTable));
        }
        req.append(";");
        return sendQuery(String.valueOf(req));
    }

    private void appendValuesToWhere(String[] names, String[] signs, String[] values, StringBuilder req) {
        for (int i = 0; i < names.length;i++) {
            req.append(names[i]).append(signs[i]).append(values[i]).append("AND");
        }
        req.delete(req.length()-3, req.length());
        req.append(";");
        sendQueryUpdate(String.valueOf(req));
    }

    private void delete(String nameTable,String[]names, String[]signs, String[]values){
        StringBuilder req = new StringBuilder(String.format("DELETE FROM %s WHERE ", nameTable));
        appendValuesToWhere(names, signs, values, req);
    }

    private void updateName(String nameTable,String[]names, String[]signs, String[]values,String nameToUpdate){
        StringBuilder req = new StringBuilder(String.format("Update %s SET Nom = '%s' WHERE ", nameTable,nameToUpdate));
        appendValuesToWhere(names, signs, values, req);
    }

    private void insertIngredientInShoppingList(int courseId, int ingredientId, int quantity) {
        String[] values = {String.format("%d",courseId),String.format("%d",ingredientId),String.format("%d",quantity)};
        insert("ListeCourseIngredient",values);
    }

    /**
     * @param table Table dans laquelle on cherche le nom
     * @param id ID correspondant au nom
     * @param nameID nom de la colonne contenant l'ID
     * @return Nom correspondant à l'ID
     * @throws SQLException
     */
    private String getNameFromID(String table, int id, String nameID) throws SQLException {
        String[]names = {nameID};
        String[]signs = {"="};
        String[]values = {String.format("%d", id)};
        ResultSet res = select(table, names, signs, values);
        res.next();
        return res.getString("Nom");
    }

    /**
     * Methode inverse de getNameFromID
     */
    private int getIDFromName(String table, String name, String nameIDColumn) throws SQLException {
        String[]names = {"Nom"};
        String[]signs = {"="};
        String[]values = {String.format("'%s'", name)};
        ResultSet res = select(table, names, signs, values);
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
    public void insertCategory(String nameCategory){
        String[] val = {"null", String.format("'%s'", nameCategory)};
        insert("Categorie", val);
    }

    public void insertType(String nameType){
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
    }

    public void insertUnite(String name){
        String[] values = {"null",String.format("'%s'",name)};
        insert("Unite",values);
    }

    public void insertFamilleAliment(String name){
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
        String[] name = {"IngredientID"};
        String[] signs = {"="};
        String[] values = {String.format("%d", idProduct)};
        ResultSet querySelectProduct = select("Ingredient", name,signs,values);
        querySelectProduct.next();
        String nameProduct = querySelectProduct.getString("Nom");
        String familleProduct = getNameFromID("FamilleAliment",querySelectProduct.getInt("FamilleAlimentID"),"FamilleAlimentID");
        String uniteProduct = getNameFromID("Unite",querySelectProduct.getInt("UniteID"),"UniteID");
        return new Product(nameProduct,1,uniteProduct,familleProduct);
    }

    /**
     * @return ArrayList contenant le nom de toutes les categories
     * @throws SQLException
     */
    public ArrayList<String> getAllCategories() throws SQLException {
        String[] vide = new String[0];
        ResultSet res = select("Categorie", vide, vide, vide);
        ArrayList<String> categories = new ArrayList<>();
        while (res.next()){
            categories.add(res.getString("Nom"));
        }
        return categories;
    }

    public ArrayList<String> getAllShoppingListName() throws SQLException {

        String[] vide = new String[0];
        ResultSet request = select("ListeCourse",vide,vide,vide);
        ArrayList<String> shoppingListName = new ArrayList<>();
        while(request.next()){
            shoppingListName.add(request.getString("Nom"));
        }
        return shoppingListName;
    }
    // TODO : PROBLEME VERIFIER SI TYPE ET CATEGORIE EXISTENT SINON MAYBE LES CREER? THROW? IDK

    public ArrayList<Recipe> getRecipeWhereCategorie(String nameCategory) throws SQLException {

        String[] name = {"Nom"};
        String[] signs = {"="};
        String[] values = {String.format("'%s'", nameCategory)};
        int idCategory = getIDFromName("Categorie",nameCategory,"CategorieID");

        ResultSet result = sendQuery(String.format("SELECT R.RecetteID, R.Nom, R.Duree, R.NbPersonnes, R.Preparation,Categorie.Nom, TypePlat.Nom\n" +
                "FROM Recette as R\n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n" +
                "WHERE R.CategorieID = %d", idCategory));

        return getRecipes(result);
    }

    private ArrayList<String> getAllNameFromTable(String table) throws SQLException {
        String[] vide = new String[0];
        ResultSet queryAllProductName = select(table,vide,vide,vide);
        ArrayList<String> allProductName = new ArrayList<>();
        while(queryAllProductName.next()){
            allProductName.add(queryAllProductName.getString("Nom"));
        }
        return allProductName;
    }

    public ArrayList<String> getAllProductName() throws SQLException {
        return getAllNameFromTable("Ingredient");
    }

    public ArrayList<String> getAllUniteName() throws SQLException {
        return getAllNameFromTable("Unite");
    }

    public ShoppingList getShoppingListFromName(String nameShoppingList) throws SQLException {

        int idName = getIDFromName("ListeCourse",nameShoppingList,"ListeCourseID");
        String[] name = {"ListeCourseID"};
        String[] signs = {"="};
        String[] values = {String.format("%d", idName)};
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

    public ArrayList<Recipe> getRecipeWhere(String nameCategory, String nameType, int nbPerson) throws SQLException {

        ArrayList<String> where = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT R.RecetteID, R.Nom, R.Duree, R.NbPersonnes, R.Preparation, Categorie.Nom, TypePlat.Nom\n" +
                "FROM Recette as R\n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n" +
                "WHERE ");

        if (nameCategory != null){
            int idCategory = getIDFromName("Categorie",nameCategory,"CategorieID");
            where.add(String.format("R.CategorieID = %d", idCategory));
        }
        if (nameType != null){
            int idType = getIDFromName("TypePlat",nameType,"TypePlatID");
            where.add(String.format("R.TypePlatID = %d", idType));
        }
        if (nbPerson > 0){
            where.add(String.format("R.NbPersonnes = %d", nbPerson));
        }

        for (int i = 0; i < where.size(); i++) {
            query.append(where.get(i)).append(" AND");
        }
        if (where.size() > 0)
            query.delete(query.length()-3, query.length());

        ResultSet result = sendQuery(query.toString());
        return getRecipes(result);
    }

    public Integer createAndGetIdShoppingList(String name) {
        String[] values = {"null",name};
        insert("ListeCourse",values);
        return getkey();
    }

    public void saveNewShoppingList(ShoppingList shoppingList) throws SQLException {
        int id = createAndGetIdShoppingList(shoppingList.getName());
        for(Product product : shoppingList){
            int idProduct = getIDFromName("Ingredient", product.getName(),"IngredientID");
            insertIngredientInShoppingList(id,idProduct, product.getQuantity());
       }
   }

   public void saveModifyShoppingList(ShoppingList shoppingList) throws SQLException {
       String[] name = {"ListeCourseID"};
       String[] signs = {"="};
       String[] values = {String.format("%d", shoppingList.getId())};
       delete("ListeCourseIngredient", name, signs, values);
       updateName("ListeCourse",name,signs,values, shoppingList.getName());
       for (Product product : shoppingList) {
           int idProduct = getIDFromName("Ingredient", product.getName(), "IngredientID");
           insertIngredientInShoppingList(shoppingList.getId(), idProduct, product.getQuantity());
       }
   }

}