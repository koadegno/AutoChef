package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Database;

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
    @Deprecated
    public List<String> getAllName() throws SQLException {
        System.out.println("NE PAS UTILISER"); //TODO ne pas utiliser
        return null;
    }


    @Override
    public void insert(User user) throws SQLException {
        String userID  = (user.getID() == -1) ? "null": String.valueOf(user.getID());
        String[] values = {userID
                ,String.format("'%s'",user.getName())
                ,String.format("'%s'",user.getFamilyName())
                ,String.format("'%s'",user.getPseudo())
                ,String.format("'%s'",user.getPassword())
                ,String.format("%d", (user.isProfessional())? TRUE : FALSE)};
        insert(USER_TABLE_NAME,values);
        userID = String.valueOf(getGeneratedID());
        insertUserAddress(userID,user.getAdress());
    }

    private void insertUserAddress(String userID, Address address) throws SQLException {
        String[] values = {String.format("%s",userID)
                ,String.format("'%s'", address.getCountry())
                ,String.format("'%s'", address.getCity())
                ,String.format("%d", address.getPostalCode())
                ,String.format("'%s'", address.getStreetName())
                ,String.format("%d", address.getHouseNumber())};
        insert(ADDRESS_TABLE_NAME,values);
    }

    @Override
    @Deprecated
    public void update(User user) throws SQLException {

    }

    public void update(String[] columnValues, String userID){
        //TODO doit on faire un truc de modification ?
        /*
        exemple de mofication :
            update AdresseMail set Nom = "jesuis@modifier.com"
            where ID = 2
         */
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
        else{
            System.out.println("je ne fonctionne pas");
            //TODO lancer une errreur
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
