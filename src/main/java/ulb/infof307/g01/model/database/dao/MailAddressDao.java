package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les adresses email favorites
 */
public class MailAddressDao extends Database implements Dao<String> {
    public static final String TABLE_MAIL_ADDRESS = "AdresseMail";
    public static final String TABLE_USER_MAIL_ADDRESS = "UtilisateurMailFavoris";


    //TODO MODIFIER STRING Pour mettre une classe Adresse mail ! si besoin
    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public MailAddressDao(String nameDB) {
        super(nameDB);
    }

    @Override
    public List<String> getAllName() throws SQLException {
        return getAllNameFromTable(TABLE_MAIL_ADDRESS,"ORDER BY Nom ASC");
    }

    /**
     * insert une adresse mail dans la base de donnée
     * @param mailAddressName l'adresse mail a inséré
     * @throws SQLException erreur liée à de la requête
     */
    @Override
    public void insert(String mailAddressName) throws SQLException {
        List<String> mailsInserted = getAllName();
        if(!mailsInserted.contains(mailAddressName)){
            insert(TABLE_MAIL_ADDRESS, new String[]{"null",String.format("'%s'",mailAddressName)});
        }
        else{
            //TODO renvoyer une erreur
            System.out.println("Le mail existe deja bg");
        }

    }

    /**
     * Insert une adresse mail pour un utilisateur spécifique
     * @param mailAddressName l'addresse mail a inséré
     * @param userID l'id de l'utilisateur
     * @throws SQLException exception lié à la base de donnée
     */
    public void insert(String mailAddressName,int userID) throws SQLException {
        List<String> mailsInserted = getAllName(userID);
        if(!mailsInserted.contains(mailAddressName)){
            insert(TABLE_MAIL_ADDRESS, new String[]{"null",String.format("'%s'",mailAddressName)});
            int mailID = getGeneratedID();
            insert(TABLE_USER_MAIL_ADDRESS, new String[]{String.valueOf(userID),String.valueOf(mailID)});
        }
        else{
            //TODO renvoyer une erreur
            System.out.println("Le mail existe deja bg");
        }
    }

    /**
     * Renvoyer toutes les adresses mails favoris d'un utilisateur en particulier
     * @param userID l'identifiant de l'utilisateur
     * @return la liste de tous les mails. Peut-être vide
     * @throws SQLException erreur lié à la base de donnée
     */
    public List<String> getAllName(int userID) throws SQLException {
        String query = String.format("""
                SELECT AdresseMail.Nom
                FROM UtilisateurMailFavoris as UMF
                INNER JOIN AdresseMail ON UMF.AdresseMailID = AdresseMail.AdresseMailID
                WHERE UMF.UtilisateurID = %d""",userID);
        ResultSet querySelectUserMail = sendQuery(query);
        List<String> userMails = new ArrayList<>();
        while (querySelectUserMail.next()){ // attention peut etre vide
            userMails.add(querySelectUserMail.getString(1));
        }
        return userMails;
    }

    @Override
    @Deprecated
    public void update(String s) throws SQLException{
        throw new IllegalCallerException("Cette methode n'est pas implementé");
    }

    @Override
    public String get(String mailAddressName) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s = '%s'","Nom",mailAddressName));
        PreparedStatement statement = select(TABLE_MAIL_ADDRESS,constraint,null);
        ResultSet querySelectProduct = sendQuery(statement);
        if(!querySelectProduct.next()) return null;
        return querySelectProduct.getString("Nom");

    }

    public void delete(String mailAddressName) throws SQLException {
        String[] constraint = {String.format("Nom = '%s'",mailAddressName)};
        super.delete(TABLE_MAIL_ADDRESS, List.of(constraint));
    }

}
