package ulb.infof307.g01.db.dao;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.util.NotImplemented;
import ulb.infof307.g01.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MailAddressDao extends Database implements Dao<String> {
    public static final String TABLE_MAIL_ADDRESS = "AdresseMail";

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

    @Override
    public void insert(String mailAddressName) throws SQLException {
        insert(TABLE_MAIL_ADDRESS, new String[]{"null",String.format("'%s'",mailAddressName)});

    }

    @Override
    @NotImplemented
    public void update(String s) throws SQLException {
        throw new NotImplementedException("Cette methode n'est pas implementé");

    }

    @Override
    public String get(String mailAddressName) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s = '%s'","Nom",mailAddressName));
        ResultSet querySelectProduct = select(TABLE_MAIL_ADDRESS,constraint,null);
        if(!querySelectProduct.next()) return null;
        return querySelectProduct.getString("Nom");

    }

    public void delete(String mailAddressName) throws SQLException {
        String[] constraint = {String.format("Nom = '%s'",mailAddressName)};
        super.delete(TABLE_MAIL_ADDRESS, List.of(constraint));
    }

}
