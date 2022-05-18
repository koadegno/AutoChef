package ulb.infof307.g01.model.database;
import ulb.infof307.g01.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
     * Le programme s'arrête si aucune connexion ne peut être établie avec une base de données.
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public Database(String nameDB) {
        String dbName = "jdbc:sqlite:" + nameDB;
        File file = new File(nameDB);
        boolean fileExist = file.exists();

        if(connection != null && request != null){
            return;
        }
        try {
            connection = DriverManager.getConnection(dbName);
            request = connection.createStatement();
            if(! fileExist ) {
                createDB();
            }
        } catch (SQLException | FileNotFoundException e) {
            try {
                request.close();
                connection.close();
                file.delete();
            } catch (SQLException ignored) {}
            System.exit(1);
        }
    }


    /**
     * Méthode de création d'une base de données qui lit et execute
     * ligne par ligne le fichier DDLDatabase.txt qui représente le ddl.
     * Si le fichier DDLDatabase.txt n'existe pas, la création est impossible
     */
    private void createDB() throws FileNotFoundException, SQLException {
        InputStream fileDb = getClass().getClassLoader().getResourceAsStream("ulb/infof307/g01/model/database/DDLDatabase.txt");
        if (fileDb != null) {
            try (Scanner scanner =new Scanner(fileDb); ){
                while(scanner.hasNextLine()){
                    request.execute(scanner.nextLine());
                }
            }catch (SQLException e){
                throw new SQLException(e);
            }
        }
        else throw new FileNotFoundException();
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
     * Requete d'accès en lecture de la base de données
     * @param statement requete sql
     * @return resultat de la requete, ou null si la requete echoue
     */
    protected ResultSet sendQuery(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    protected ResultSet sendQuery(String query) throws SQLException { //to delete after uniformisation
        return request.executeQuery(query);
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
    protected Integer getGeneratedID() throws SQLException {
        ResultSet getID = request.getGeneratedKeys();
        getID.next();
        return getID.getInt(1);
    }

    /**
     * Permet d'analyser le contenu de la requête en fonction des types
     * des valeurs et de les placer dans le PreparedStatement
     * @param statement le PreparedStatement à remplir
     * @param valueOfPreparedStatement les types à déterminer
     */
    protected void fillPreparedStatementValues(PreparedStatement statement, List<String> valueOfPreparedStatement) throws SQLException {
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
        try (PreparedStatement statement = connection.prepareStatement(appendValuesToWhere(query, constraintToAppend))) {
            sendQueryUpdate(statement);
        }
    }


    protected String appendValuesToWhere(StringBuilder query, List<String> constraintToAppend) {
        for (int i = 0; i < constraintToAppend.size(); i++) {
            String constraint = constraintToAppend.get(i);
            query.append(constraint);
            if(i != constraintToAppend.size()-1)query.append(" AND ");

        }
        return String.valueOf(query);
    }


    /**
     * @param table Table dans laquelle on cherche le nom
     * @param id ID correspondant au nom
     * @param nameIDColumn nom de la colonne contenant l'ID
     * @return Nom correspondant à l'ID
     */
    protected String getNameFromID(String table, int id, String nameIDColumn) throws SQLException {
        int columnIndexToRetrieve = 1;
        String query = String.format("""
                        SELECT Nom FROM %s
                        WHERE %s = %s
                """, table,nameIDColumn, id);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet =  sendQuery(statement);
            if(!resultSet.next())throw new SQLException();
            return resultSet.getString(columnIndexToRetrieve);
        }
    }

    /**
     * Methode inverse de getNameFromID
     */
    protected int getIDFromName(String table, String name) throws SQLException {
        int columnIndexToRetrieve = 1;
        String query = String.format("""
                        SELECT * FROM %s
                        WHERE Nom = ?
                """, table);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(columnIndexToRetrieve, name);
            ResultSet resultSet =  sendQuery(statement);
            if(!resultSet.next())throw new SQLException();
            return resultSet.getInt(columnIndexToRetrieve);
        }
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

            Product product = new Product.ProductBuilder().withName(productName).withQuantity(quantity).withNameUnity(unityName).withFamilyProduct(familyName).build();
            recipe.add(product);
        }
    }

    protected List<String> getListOfName(String query) throws SQLException {
        List<String> nameList;
        int columnIndex = 1;
        try (ResultSet queryAllName = sendQuery(query)) {
            nameList = new ArrayList<>();
            while (queryAllName.next()) {
                nameList.add(queryAllName.getString(columnIndex));
            }
        }
        return nameList;
    }

    protected static int getUserID() {
        return Configuration.getCurrent().getCurrentUser().getId();
    }

    protected void insertNameWithPreparedStatement(String name, String table) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (null,?);
            """,table);
        sendInsertNameQueryWithPreparedStatement(name, query);
    }

    protected void sendInsertNameQueryWithPreparedStatement(String name, String query) throws SQLException {
        int nameIndexInPreparedStatement = 1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(nameIndexInPreparedStatement, name);
            sendQueryUpdate(statement);
        }
    }

    protected  <T extends ProductHashSet> void insertListOfProducts(T vectorOfProduct, int objectId, String table) throws SQLException {
        int quantityIndexInPreparedStatement = 1;
        for (Product p: vectorOfProduct) { // ajout des produits lié a la recette
            int productID = getIDFromName("Ingredient", p.getName());
            int quantity = p.getQuantity();
            String query = String.format("""
            INSERT INTO %s values (%s, %s, ?);
            """,table, objectId,productID);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(quantityIndexInPreparedStatement, quantity);
                sendQueryUpdate(statement);
            }
        }
    }

}