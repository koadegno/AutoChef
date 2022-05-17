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


    //TODO: MODIFIER STRING Pour mettre une classe Adresse mail ! si besoin
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
        String query = String.format("""
                SELECT AdresseMail.Nom
                FROM UtilisateurMailFavoris as UMF
                INNER JOIN AdresseMail ON UMF.AdresseMailID = AdresseMail.AdresseMailID
                WHERE UMF.UtilisateurID = %d
                """, getUserID());

        return getListOfName(query);
    }

    /**
     * insertUserMail une adresse mail dans la base de donnée
     * @param mailAddressName l'adresse mail a inséré
     * @throws SQLException erreur liée à de la requête
     */
    @Override
    public void insert(String mailAddressName) throws SQLException {
        int mailIndexInPreparedStatement = 1;
        String query = String.format("""
            INSERT INTO %s values (null, ?);
            """,TABLE_MAIL_ADDRESS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(mailIndexInPreparedStatement,mailAddressName);
            sendQueryUpdate(statement);
        }
    }

    /**
     * Insert une adresse mail pour un utilisateur spécifique
     *
     * @param mailAddressName l'addresse mail a inséré
     * @throws SQLException exception lié à la base de donnée
     */
    public void insertUserMail(String mailAddressName) throws SQLException{
        int mailID;
        try{
            insert(mailAddressName);
            mailID = getGeneratedID();

        } catch (SQLException e) {// cas ou adresse mail multiple dans la db
            mailID = Integer.parseInt(get(mailAddressName));
        }
        String query = String.format("""
            INSERT INTO %s values (%s, %s);
            """,TABLE_USER_MAIL_ADDRESS,getUserID(),mailID);
        System.out.println(query);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            sendQueryUpdate(statement);
        }
    }

    @Override
    public void update(String s) throws SQLException{
        throw new IllegalCallerException("Cette methode n'est pas implementé");
    }

    /**
     * Get fait sur base de l'adresse mail
     * @param mailAddressName l'adresse mail
     * @return l'id de l'adresse mail, null sinon
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public String get(String mailAddressName) throws SQLException {
        return get(String.format("%s = '%s'", "Nom", mailAddressName), "AdresseMailID");

    }

    /**
     * Get fait sur base de l'id du mail
     * @param mailID id du mail
     * @return l'adresse mail qui corresponds a l'id ou null sinon
     * @throws SQLException erreur liée à la base de donnée
     */
    public String get(int mailID) throws SQLException {
        return get(String.format("%s = %d", "AdresseMailID", mailID), "Nom");

    }

    /**
     * Generique get qui permet de recupere une infotmation en fonction d'une contrainte et
     * d'une column specifique
     * @param constraint la containte sur laquelle on va chercher
     * @param tableName la colonne sur la quelle on va recupere l'information
     * @return l'information voulue ou null si elle n'existe pas
     * @throws SQLException erreur liée à la requête
     */
    private String get(String constraint, String tableName) throws SQLException {
        ArrayList<String> constraints = new ArrayList<>();
        constraints.add(constraint);
        PreparedStatement statement = select(TABLE_MAIL_ADDRESS,constraints,null);
        ResultSet querySelectProduct = sendQuery(statement);
        if(!querySelectProduct.next()) return null;
        return querySelectProduct.getString(tableName);
    }

}
