package ulb.infof307.g01.db;

import java.sql.*;

public class Database {
    private String dbName;
    private Connection connection;
    private Statement request;

    public Database(String nameDB) {
        dbName = "jdbc:sqlite:" + nameDB;
        try {
            connection = DriverManager.getConnection(dbName);
            request = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    private Integer createAndGetIdShoppingList(String query) {
        try {
            request.executeUpdate(query);
            ResultSet getKey = request.getGeneratedKeys();
            getKey.next();
            return getKey.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                "    ListeCourseID INTEGER PRIMARY KEY AUTOINCREMENT " +
                ");\n";
        return sendRequest(req);
    }

    public Integer createIdShoppingList() {
        String req = "INSERT INTO ListeCourse values (null)";
        return createAndGetIdShoppingList(req);
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
}