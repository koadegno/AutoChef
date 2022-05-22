package ulb.infof307.g01.model.database.dao;

import org.jetbrains.annotations.Nullable;
import ulb.infof307.g01.model.database.Database;
import ulb.infof307.g01.model.exception.DuplicatedKeyException;

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
     * Insert une adresse mail pour un utilisateur spécifique
     *
     * @param mailAddressName l'addresse mail a inséré
     * @throws SQLException exception lié à la base de donnée
     */
    @Override
    public void insert(String mailAddressName) throws SQLException, DuplicatedKeyException {
        String id = get(mailAddressName);
        int mailID;
        if(id == null) mailID = insertNameWithPreparedStatement(mailAddressName, TABLE_MAIL_ADDRESS);
        else mailID = Integer.parseInt(id);
        String query = String.format("""
            INSERT INTO %s values (%s, %s);
            """,TABLE_USER_MAIL_ADDRESS,getUserID(),mailID);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            sendQueryUpdate(statement);
        } catch (SQLException e) {
            throw new DuplicatedKeyException("L'adresse mail existe déja dans la base de donnée");
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
        String query = String.format("""
                        SELECT AdresseMailID FROM %s
                        WHERE nom = ?
                """, TABLE_MAIL_ADDRESS);
        return sendGetQuery(query, mailAddressName); //mailAddressName peut provenir de l'utilisateur
    }

    @Nullable
    private String sendGetQuery(String query, String stringForPreparedStatement) throws SQLException {
        int columnIndexToRetrieve = 1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if(stringForPreparedStatement != null)statement.setString(columnIndexToRetrieve, stringForPreparedStatement);
            ResultSet querySelectProduct =  sendQuery(statement);
            if(!querySelectProduct.next()) return null;
            return querySelectProduct.getString(columnIndexToRetrieve);
        }
    }

    /**
     * Get fait sur base de l'id du mail
     * @param mailID id du mail
     * @return l'adresse mail qui corresponds a l'id ou null sinon
     * @throws SQLException erreur liée à la base de donnée
     */
    public String get(int mailID) throws SQLException {
        String query = String.format("""
                        SELECT Nom FROM %s
                        WHERE AdresseMailID = %s
                """, TABLE_MAIL_ADDRESS, mailID);
        return sendGetQuery(query,null); //mailID provient pas de l'utilisateur

    }
}
