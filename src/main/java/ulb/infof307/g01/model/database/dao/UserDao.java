package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe d'accès a la base de donnée permettant l'accès aux informations des utilisateurs
 */
public class UserDao extends Database implements Dao<User>{

    public static final int TRUE = 1;
    public static final int FALSE = 0;
    private static final String USER_TABLE_NAME = "Utilisateur";
    private static final String ADDRESS_TABLE_NAME = "UtilisateurAdresse";

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public UserDao(String nameDB) {
        super(nameDB);
    }


    @Override
    public List<String> getAllName() throws SQLException {
        return null;
    }


    @Override
    public void insert(User user) throws SQLException {
        String userID  = (user.getId() == -1) ? "null": String.valueOf(user.getId());
        insertUser(user, userID);
        userID = String.valueOf(getGeneratedID());
        insertUserAddress(userID,user.getAddress());
    }

    private void insertUser(User user, String userID) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (%s,?,?,?,?,%S);
            """,USER_TABLE_NAME, userID,(user.isProfessional()? TRUE : FALSE) );
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getFamilyName());
            statement.setString(3, user.getPseudo());
            statement.setString(4, user.getPassword());
            sendQueryUpdate(statement);
        }
    }

    private void insertUserAddress(String userID, Address address) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (%s,?,?,?,?,?);
            """,ADDRESS_TABLE_NAME, userID);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, address.getCountry());
            statement.setString(2, address.getCity());
            statement.setInt(3, address.getPostalCode());
            statement.setString(4, address.getStreetName());
            statement.setInt(5, address.getHouseNumber());
            sendQueryUpdate(statement);
        }
    }

    @Override
    public void update(User user) throws SQLException {
        throw new IllegalCallerException("Cette methode n'est pas implémenté");
    }

    @Override
    public User get(String userPseudo) throws SQLException {
        User user = null;
        ResultSet querySelectUser = sendQuery(String.format("""
                SELECT U.UtilisateurID, U.Prenom, U.Nom, U.MotDePasse, U.estProfessionnel, UtilisateurAdresse.Pays, UtilisateurAdresse.Ville, UtilisateurAdresse.CodePostal, UtilisateurAdresse.RueNom, UtilisateurAdresse.RueNumero
                FROM Utilisateur as U
                LEFT JOIN UtilisateurAdresse ON U.UtilisateurID = UtilisateurAdresse.UtilisateurID
                WHERE U.Pseudo = '%s'""", userPseudo));

        if(querySelectUser.next()){
            user = ExtractUserFromQuery(userPseudo, querySelectUser);
        }
        return user;
    }

    /**
     * Remplie un Objet User à partir d'un ResultSet concernant un utilisateur
     * @param userPseudo le pseudo de l'utilisateur
     * @param querySelectUser la requete contenant les infos de l'utilisateur
     * @return User l'objet représentant utilisateur
     * @throws SQLException erreur avec le ResultSet
     */
    private User ExtractUserFromQuery(String userPseudo, ResultSet querySelectUser) throws SQLException {
        User user;
        int userID = querySelectUser.getInt(1);
        String userFirstname = querySelectUser.getString(2);
        String userLastname = querySelectUser.getString(3);
        String userPassword = querySelectUser.getString(4);
        Boolean userIsProfessional = querySelectUser.getBoolean(5);
        String userCountry = querySelectUser.getString(6);
        String userCity = querySelectUser.getString(7);
        int userPostalCode = querySelectUser.getInt(8);
        String userStreetName = querySelectUser.getString(9);
        int userHouseNumber = querySelectUser.getInt(10);
        Address userAddress = new Address(userCountry,userCity,userPostalCode,userStreetName,userHouseNumber);
        user = new User(userID,userLastname,userFirstname, userPseudo,userPassword,userAddress,userIsProfessional);
        return user;
    }


}
