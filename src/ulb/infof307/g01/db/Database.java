package ulb.infof307.g01.db;
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

    public void createDB(){
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

    public boolean sendRequest(String req) {
        try {
            return request.execute(req);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet sendQuery(String query) {
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

    public boolean createTableFamilleAliment() {
        String req = "CREATE TABLE IF NOT EXISTS FamilleAliment (\n" +
                "    FamilleAlimentID INTEGER PRIMARY KEY,\n" +
                "    Nom CHAR(20) NOT NULL);";
        return sendRequest(req);
    }

    public boolean createTableUnite() {
        String req = "CREATE TABLE IF NOT EXISTS Unite(\n" +
                "    UniteID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(10) NOT NULL UNIQUE\n" +
                ")";
        return sendRequest(req);
    }

    public boolean createTableCategorie() {
        String req = "CREATE TABLE IF NOT EXISTS Categorie(\n" +
                "    CategorieID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(20) NOT NULL UNIQUE\n" +
                ")";
        return sendRequest(req);
    }

    public boolean createTableTypePlat() {
        String req = "CREATE TABLE IF NOT EXISTS TypePlat(\n" +
                "    TypePlatID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Nom CHAR(20) NOT NULL UNIQUE\n" +
                ")";
        return sendRequest(req);
    }

    public boolean createTableRecette() {
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

    public boolean createTableIngredient() {
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

    public boolean createTableRecetteIngredient() {
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

    public boolean createTableListeCourse() {
        String req = "CREATE TABLE IF NOT EXISTS ListeCourse (\n" +
                "    ListeCourseID INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                "    Nom CHAR(25) NOT NULL UNIQUE \n" +
                ");";
        return sendRequest(req);
    }

    public boolean createTableListeCourseIngredient() {
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

    public boolean createTableMenuRecette() {
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
     * Les valeurs doivent etre encode sous la forme pour les strings : "'exemple'"
     * Reste doit etre encode sous la forme : "exemple"
     *
     * @param values Les differentes valeurs a inserer dans l'ordre des colonnes
     */
    public void insert(String nameTable,String[] values){
        StringBuilder req = new StringBuilder(String.format("INSERT INTO %s values (", nameTable));
        for (String value : values) {
            req.append(value).append(",");
        }
        req.deleteCharAt(req.length()-1);
        req.append(");");
        sendQueryUpdate(String.valueOf(req));
    }

    public ResultSet select(String nameTable,String[]names, String[]signs, String[]values){
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

    public Integer createAndGetIdShoppingList(String name) {
        String[] values = {"null",name};
        insert("ListeCourse",values);
        return getkey();
    }

    private void insertIngredientInShoppingList(int courseId, int ingredientId, int quantity) {
        //TODO: si on ajoute le nom dans ListeCourse, changer le courseId par le nom
        String[] values = {String.format("%d",courseId),String.format("%d",ingredientId),String.format("%d",quantity)};
        insert("ListeCourseIngredient",values);
    }

    public ArrayList<String> getAllCategories() throws SQLException {
        String[] vide = new String[0];
        ResultSet res = select("Categorie", vide, vide, vide);
        ArrayList<String> categories = new ArrayList<>();
        while (res.next()){
            categories.add(res.getString("Nom"));
        }
        return categories;
    }

    /*
    public ArrayList<Recipe> getRecipeWhereCategorie(String nameCategory) throws SQLException {
        String[] name = {"Nom"};
        String[] signs = {"="};
        String[] values = {String.format("'%s'", nameCategory)};
        ResultSet res = select("Categorie",name, signs, values);
        res.next();
        int key = res.getInt("CategorieID");
        name[0] = "CategorieID";
        values[0] = String.format("%d", key);
        ArrayList<Recipe> recipes = new ArrayList<>();

        res = select("Recette",name, signs, values);

        while (res.next()){
            Recipe recipe = new Recipe(res.getString("Nom"), ...)
            recipes.add(recipe);
        }
        return recipes;

    }
    */

    //TODO: Pour save une shopping list quand elle sera implemente
//    public void saveShoppingList(ShoppingList shoppingList){
//        int id = createAndGetIdShoppingList(shoppingList.getName());
//        for(Produit produit : shoppingList){
//            insertIngredientInShoppingList(id,produit.getId(),produit.getQuantite());
//        }
//    }

}