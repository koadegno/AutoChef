package ulb.infof307.g01.model.database;
import ulb.infof307.g01.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Classe représentant la Base de Données
 * Permet la création d'une nouvelle base de données ou d'en charger
 * une existante en donnant son nom en paramètre du constructeur
 */
public class Database {

    protected static Connection connection=null;
    protected static Statement request=null;

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
            if(connection != null && request != null){
                return; //TODO changer ca un jour
            }
            connection = DriverManager.getConnection(dbName);
            request = connection.createStatement();
            if(! fileExist ) {
                createDB();
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO lancer une erreur
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
            e.printStackTrace(); //TODO lancer une erreur
        }
    }

    public void closeConnection() throws SQLException {
        if((request != null) && (connection != null) && (!request.isClosed() && !connection.isClosed())) {
            request.close();
            connection.close();
            request = null;
            connection = null;
        }
    }

    /**
     * Requete de la base de données uniquement pour la creation de table
     * @param query requete sql
     */
    public void sendRequest(String query) {
        try {
            request.execute(query);
        } catch (SQLException e) {
            //TODO lancer une erreur
            e.printStackTrace();
        }
    }

    /**
     * Requete d'accès en lecture de la base de données
     * @param statement requete sql
     * @return resultat de la requete, ou null si la requete echoue
     */
    protected ResultSet sendQuery(PreparedStatement statement) {
        ResultSet res = null;
        try {
            res =  statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO lancer une erreur;

        }
        return res;
    }

    protected ResultSet sendQuery(String query) { //to delete
        ResultSet res = null;
        try {
            res =  request.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace(); //TODO lancer une erreur
        }
        return res;
    }

    /**
     * Requete d'accès en écriture de la base de données
     * @param statement requete sql
     */
    protected void sendQueryUpdate(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
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
            e.printStackTrace(); //TODO lancer une erreur;
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
        for (int i = 0; i < values.length ; i++) {
            query.append("?").append(",");
        }
        query.deleteCharAt(query.length()-1).append(");");
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
        ArrayList<String> columnValues = new ArrayList<>(Arrays.asList(values));
        fillPreparedStatementValues(statement, columnValues);
        sendQueryUpdate(statement);
        statement.close();
    }

    /**
     * @param nameTable La table pour laquel il faut faire un select
     * @param constraintToAppend liste de toute les contraintes à ajouter
     * @return prepared statement
     */
    protected PreparedStatement select(String nameTable, List<String> constraintToAppend, String orderBy) throws SQLException {
        String stringQuery;
        StringBuilder query = new StringBuilder(String.format("SELECT * FROM %s ", nameTable));
        ArrayList<String> valueOfPreparedStatement;
        if (!constraintToAppend.isEmpty()) {
            query.append(" WHERE ");
        }
        valueOfPreparedStatement =  appendValuesToWhere(query,constraintToAppend);
        if(orderBy != null){
            query.append(orderBy);
        }
        query.append(';');
        stringQuery = String.valueOf(query);
        PreparedStatement statement = connection.prepareStatement(stringQuery);
        fillPreparedStatementValues(statement, valueOfPreparedStatement);

        return statement;
    }



    /**
     * Permet d'analyser le contenu de la requête en fonction des types
     * des valeurs et de les placer dans le PreparedStatement
     * @param statement le PreparedStatement à remplir
     * @param valueOfPreparedStatement les types à déterminer
     */
    protected void fillPreparedStatementValues(PreparedStatement statement, ArrayList<String> valueOfPreparedStatement) throws SQLException {
        if (valueOfPreparedStatement ==null) return;
        for(int i = 1; i < valueOfPreparedStatement.size() +1; i++){
            String columnValue = valueOfPreparedStatement.get(i-1);
            if(columnValue.contains("'")){//string value
                StringBuilder removeQuotes = new StringBuilder(columnValue.strip());
                removeQuotes.deleteCharAt(0);
                removeQuotes.deleteCharAt(removeQuotes.length() - 1);

                statement.setString(i, String.valueOf(removeQuotes));
            }
            else if(columnValue.contains(".")){ //double
                statement.setDouble(i, Double.parseDouble(columnValue));
            }
            else if(columnValue.equals(("null"))){
                statement.setNull(i, Types.NULL);
            }
            else{ //int value
                int val =  Integer.parseInt(columnValue.strip());
                statement.setInt(i,val);
            }
        }
    }

    protected void delete(String nameTable, List<String> constraintToAppend) throws SQLException {
        StringBuilder query = new StringBuilder(String.format("DELETE FROM %s WHERE ", nameTable));
        ArrayList<String> valueOfPreparedStatement = appendValuesToWhere(query, constraintToAppend);
        query.append(";");
        String stringQuery = String.valueOf(query);
        PreparedStatement statement = connection.prepareStatement(stringQuery);
        fillPreparedStatementValues(statement, valueOfPreparedStatement);
        sendQueryUpdate(statement);
    }

    /**
     * Remplissage automatique d'une requête avec des contraintes
     * @param query requete à contraindre
     * @param constraintToAppend contraintes à ajouter à requête
     * @return requete avec contrainte
     */
    protected ArrayList<String> appendValuesToWhere(StringBuilder query, List<String> constraintToAppend) {
        ArrayList<String> columnValues = new ArrayList<>();
        if (!constraintToAppend.isEmpty()) {
            for (String s : constraintToAppend) {
                ArrayList<String> columnAndValue = splitOnCharacter(s);
                query.append(columnAndValue.get(0)).append(columnAndValue.get(2)).append(" ?").append(" AND ");
                columnValues.add(columnAndValue.get(1));
            }
            query.delete(query.length() - 4, query.length());
        }
        return columnValues;
    }

    protected void updateName(String nameTable, String nameToUpdate, List<String> constraintToAppend) throws SQLException {
        StringBuilder query = new StringBuilder(String.format("Update %s SET Nom = '%s' WHERE ", nameTable, nameToUpdate));
        //nameTable,nameToUpdate
        ArrayList<String>  valuesOfPreparedStatement = appendValuesToWhere(query, constraintToAppend);
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));

        fillPreparedStatementValues(statement, valuesOfPreparedStatement);
        sendQueryUpdate(statement);
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
        PreparedStatement statement = select(table,constraint,null);
        ResultSet res = sendQuery(statement);
        res.next();
        return res.getString("Nom");
    }

    /**
     * Methode inverse de getNameFromID
     */
    protected int getIDFromName(String table, String name, String nameIDColumn) throws SQLException {

        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s='%s'","Nom",name));
        PreparedStatement statement =  select(table,constraint,null);

        ResultSet res = sendQuery(statement);

        res.next();//TODO lancer une erreur

        return res.getInt(nameIDColumn);
    }

    /**
     *
     * @param orderBy si non nul, ajoute la contrainte de triée par
     */
    protected ArrayList<String> getAllNameFromTable(String table, String orderBy) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        PreparedStatement statement =  select(table, constraint,orderBy);
        ResultSet queryAllTableName = sendQuery(statement);
        ArrayList<String> allProductName = new ArrayList<>();
        while(queryAllTableName.next()){
            allProductName.add(queryAllTableName.getString("Nom"));
        }
        return allProductName;
    }

    protected void fillRecipeWithProducts(Recipe recipe) throws SQLException {
        ResultSet querySelectProduct = sendQuery(String.format("""
                SELECT I.Nom, F.Nom, U.Nom, R.Quantite
                FROM RecetteIngredient as R
                INNER JOIN Ingredient as I ON R.IngredientID = I.IngredientID\s
                INNER JOIN FamilleAliment as F ON F.FamilleAlimentID = I.FamilleAlimentID
                INNER JOIN Unite as U ON U.UniteID = I.UniteID
                WHERE R.RecetteID = %d""", recipe.getId()));

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
     * Découpe chaque contrainte d'une requête sous forme de String en un ArrayList de 3 Strings
     * @param s string a split
     * @return list de 3 strings
     */
    protected ArrayList<String> splitOnCharacter(String s){
        ArrayList<String> parts = null;
        if(s.contains(">=")){
            //split sur base de >=
            parts = new ArrayList<>(Arrays.asList(s.split(">=")));
            parts.add(">=");
        }
        else if(s.contains("<=")){
            parts = new ArrayList<>(Arrays.asList(s.split("<=")));
            parts.add("<=");
        }
        else if(s.contains("<")){
            parts = new ArrayList<>(Arrays.asList(s.split("<")));
            parts.add("<");
        }
        else if(s.contains(">")){
            parts = new ArrayList<>(Arrays.asList(s.split(">")));
            parts.add(">");
        }
        else if(s.contains("=")){
            parts = new ArrayList<>(Arrays.asList(s.split("=")));
            parts.add("=");
        }
        return parts;
    }

}