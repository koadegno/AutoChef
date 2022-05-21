package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.database.Database;
import ulb.infof307.g01.model.ShoppingList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les listes de courses
 */
public class ShoppingListDao extends Database implements Dao<ShoppingList> {

    public static final String LISTE_COURSE_TABLE_NAME = "ListeCourse";
    public static final String LISTE_COURSE_INGREDIENT_TABLE_NAME = "ListeCourseIngredient";
    public static final String LISTE_COURSE_USER_TABLE_NAME = "UtilisateurListeCourse";

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public ShoppingListDao(String nameDB) {
        super(nameDB);
    }


    @Override
    public List<String> getAllName() throws SQLException {
        String query = String.format("""
                SELECT R.Nom
                FROM ListeCourse as R
                INNER JOIN UtilisateurListeCourse ON R.ListeCourseID = UtilisateurListeCourse.ListeCourseID
                WHERE UtilisateurListeCourse.UtilisateurID = %d
                ORDER BY Nom ASC
                """, getUserID());
        return getListOfName(query);
    }

    /**
     * insertUserMail une liste de course dans la base de donnée
     * @param shoppingList liste de course à insérer
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public void insert(ShoppingList shoppingList) throws SQLException {
        insertNameWithPreparedStatement(shoppingList.getName(), LISTE_COURSE_TABLE_NAME);
        int shoppingListID = getGeneratedID();
        insertListOfProducts(shoppingList, shoppingListID,LISTE_COURSE_INGREDIENT_TABLE_NAME, false );
        insertUserShoppingList(shoppingListID);
    }

    private void insertUserShoppingList(int shoppingListID) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (%s, %s);
            """,LISTE_COURSE_USER_TABLE_NAME, getUserID() , shoppingListID);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            sendQueryUpdate(statement);
        }
    }

    /**
     * Sauvegarde une liste de course modifiée, et la supprime si la liste ne contient rien
     */
    @Override
    public void update(ShoppingList shoppingList) throws SQLException {
        int shoppingListID = getIDFromName(LISTE_COURSE_TABLE_NAME, shoppingList.getName());
        String idColumnName = "ListeCourseID";
        deleteByID(shoppingListID, LISTE_COURSE_INGREDIENT_TABLE_NAME, idColumnName);

        String query = String.format("""
                        UPDATE %s SET Nom = ?
                        WHERE ListeCourseID = %s
                """, LISTE_COURSE_TABLE_NAME,shoppingListID);
        sendInsertNameQueryWithPreparedStatement(shoppingList.getName(),query); //update query


        if(shoppingList.size() == 0){
            deleteByID(shoppingListID, LISTE_COURSE_TABLE_NAME, idColumnName);
        }
        else{
            insertListOfProducts(shoppingList,shoppingListID,LISTE_COURSE_INGREDIENT_TABLE_NAME, false );
        }
    }

    @Override
    public ShoppingList get(String name) throws SQLException {
        int userID = getUserID();
        String query = String.format("""
                SELECT  S.ListeCourseID
                FROM ListeCourse as S
                INNER JOIN UtilisateurListeCourse ON UtilisateurListeCourse.ListeCourseID = S.ListeCourseID
                WHERE UtilisateurListeCourse.UtilisateurID = %s AND s.Nom = ?""",userID); //verifie si la liste de course appartien à l'utilisateur
        int nameIndexInPreparedStatement = 1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(nameIndexInPreparedStatement, name);
            ResultSet resultSet = sendQuery(statement);
            if(!resultSet.next()) return null;
            int shoppingListID = resultSet.getInt(nameIndexInPreparedStatement);
            ShoppingList shoppingList = new ShoppingList(name,shoppingListID);
            String idColumnName = "ListeCourseID";
            fillProductHashset(shoppingList, shoppingListID,LISTE_COURSE_INGREDIENT_TABLE_NAME,idColumnName, false );
            return shoppingList;
        }
    }
}
