package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class UserDao extends Database implements Dao<User>{

    private static String USER_TABLE_NAME = "Utilisateur";
    private static String ADDRESS_TABLE_NAME = "UtilisateurAdresse";
    private static String[] COLUMN_NAME = {"Nom"}; //TODO rajouter le nom des colonnes si on veut modifier

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
        System.out.println("NE PAS UTILISER");
        return null;
    }

    @Override
    public void insert(User user) throws SQLException {
        String userID  = (user.getID() == -1) ? "null": String.valueOf(user.getID());
        String[] values = {userID
                ,String.format("%s",user.getFamilyName())
                ,String.format("%s",user.getPseudo())
                ,String.format("%s",user.getPassword())
                ,String.format("%s",user.isProfessional())};
        insert(USER_TABLE_NAME,values);
        userID = String.valueOf(getIDFromName(USER_TABLE_NAME, user.getPseudo(), "Pseudo"));
        insertUserAddress(userID,user.getAdress());
    }

    private void insertUserAddress(String userID, Address address) throws SQLException {
        String[] values = {String.format("%d",userID)
                ,String.format("%s", address.getCountry())
                ,String.format("%s", address.getCity())
                ,String.format("%d", address.getPostalCode())
                ,String.format("%s", address.getStreetName())
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
    public User get(String UserID) throws SQLException {
        //TODO est ce que le get et le update son necessaire ?
        ResultSet querySelectUser = sendQuery(String.format("""
                SELECT U.Prenom, 
                FROM Utilisateur as U
                INNER JOIN UtilisateurAdresse ON U.UtilisateurAdresse = UtilisateurAdresse.UtilisateurID
                WHERE U.UtilisateurAdresse = %s""", UserID));

        if(querySelectUser.next()){
            //TODO remplir
        }

        return null;
    }


}
