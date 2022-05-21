package ulb.infof307.g01.model.database;
import ulb.infof307.g01.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
     *  Supprime toute les lignes de la table ayant ojectID comme ID
     * @param objectID l'id de la ligne à supprimer
     * @param tableName le nom de la table sur laquelle faire les suppression
     * @param idColumnName le nom de la column ayant les id dans la table
     * @throws SQLException
     */
    protected void deleteByID(int objectID, String tableName, String idColumnName) throws SQLException {
        String query = String.format("""
                    DELETE FROM %s  
                    WHERE %s = %s
                 """,tableName , idColumnName, objectID);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            sendQueryUpdate(statement);
        }
    }


    /**
     * Avoir le nom d'un objet à partir de son ID
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
     * Recuperer l'ID d'un objet sur base de son nom
     * @param table dans quel table faire la rêquete
     * @param name Nom de l'objet en question
     * @return L'ID de l'objet correspondant au nom
     * @throws SQLException
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

    /**
     * Rempli un productHashset d'ingredient
     * @param vectorOfProduct un objet ayant des produits
     * @param objectID id de l'objet
     * @param table   la table qui contient la liaison de  l'objet aux produits
     * @param idColumnName le nom de la colonne ayant l'ID de l objet
     * @param hasPrice  Pour faire la différence avec un magasin qui a besoin du prix et nom de la quantité
     * @param <T>
     * @throws SQLException
     */
    protected <T extends ProductHashSet> void fillProductHashset(T vectorOfProduct, int objectID, String table, String idColumnName, boolean hasPrice) throws SQLException {
        String columName = hasPrice ? "prix" : "Quantite";
        String query = String.format("""
                SELECT I.Nom, F.Nom, U.Nom, R.%s  
                FROM %s as R
                INNER JOIN Ingredient as I ON R.IngredientID = I.IngredientID\s
                INNER JOIN FamilleAliment as F ON F.FamilleAlimentID = I.FamilleAlimentID
                INNER JOIN Unite as U ON U.UniteID = I.UniteID
                WHERE R.%s = %d""", columName, table, idColumnName, objectID);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet =  sendQuery(statement);
            while(resultSet.next()){
                String productName = resultSet.getString(1);
                String familyName = resultSet.getString(2);
                String unityName = resultSet.getString(3);
                Product product;
                if(hasPrice) product = new Product.ProductBuilder().withName(productName).withPrice(resultSet.getDouble(4)).build();
                else product = new Product.ProductBuilder().withName(productName).withQuantity(resultSet.getInt(4)).withNameUnity(unityName).withFamilyProduct(familyName).build();
                vectorOfProduct.add(product);
            }
        }

    }

    /**
     *  elle permet de prendre que des string dans la base de donnée (Utilisé par les DAO)
     * @param query une rêque de type get
     * @return Une liste de tous les noms d'objet dans la table demandé (ex: les nom de toutes les recettes)
     * @throws SQLException
     */

    protected List<String> getListOfName(String query) throws SQLException {
        List<String> nameList;
        int columnIndex = 1;
        try (PreparedStatement statement = connection.prepareStatement(String.valueOf(query));) {
            ResultSet resultSet = sendQuery(statement);
            nameList = new ArrayList<>();
            while (resultSet.next()) {
                nameList.add(resultSet.getString(columnIndex));
            }
            return nameList;
        }
    }

    protected static int getUserID() {
        return Configuration.getCurrent().getCurrentUser().getId();
    }


    /**
     * Insert dans la base de donnée le nom d'un objet (recette, produit, liste de course ....) car c'est une
     * donnée qui vient de l'utilisateur et il faut y faire attention en matière de sécurité.
     * La méthode est trop répétés dans les DAO
     * @param name le nom de l'objet
     * @param table la table dans laquelle elle doit re inserer
     * @throws SQLException
     */
    protected void insertNameWithPreparedStatement(String name, String table) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (null,?);
            """,table);
        sendInsertNameQueryWithPreparedStatement(name, query);
    }

    /**
     * Crée un prepared statement qui aura besoin d'être rempli avec un seul string venant de l'utilisateur
     * @param name le nom de l'objet à crée ou modifier
     * @param query une requête insert toute faite avec un seul point d'interrogation pour la prepared statement
     * @throws SQLException
     */

    protected void sendInsertNameQueryWithPreparedStatement(String name, String query) throws SQLException {
        int nameIndexInPreparedStatement = 1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(nameIndexInPreparedStatement, name);
            sendQueryUpdate(statement);
        }
    }

    /**
     * Insére un lien vers des produits dans la base de donnée pour des objets contenant des produit (recipe / shoppingList)
     * @param vectorOfProduct une recette ou une liste de course précisement
     * @param objectId  l'ID de la recette ou de la liste de course dans la base de donnée
     * @param table   La table dans laquelle on doit crée le lien avec le produit (recipeIngredient / ListeDeCourseIngredient
     * @param <T>    Un type qui contient une liste de produit
     * @throws SQLException
     */
    protected  <T extends ProductHashSet> void insertListOfProducts(T vectorOfProduct, int objectId, String table, boolean hasPrice) throws SQLException { //TODO : centraliser avec shop aussi
        int quantityIndexInPreparedStatement = 1;
        for (Product p: vectorOfProduct) { // ajout des produits lié a la recette
            int productID = getIDFromName("Ingredient", p.getName());
            String query = String.format("""
            INSERT INTO %s values (%s, %s, ?);
            """,table, objectId,productID);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                if(hasPrice)statement.setDouble(quantityIndexInPreparedStatement, p.getPrice());
                else statement.setInt(quantityIndexInPreparedStatement, p.getQuantity());
                sendQueryUpdate(statement);
            }
        }
    }

}