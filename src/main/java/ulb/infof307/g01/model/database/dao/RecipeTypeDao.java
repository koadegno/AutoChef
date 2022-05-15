package ulb.infof307.g01.model.database.dao;

import ulb.infof307.g01.model.database.Database;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les types de plats
 */
public class RecipeTypeDao extends Database implements Dao<String> {
    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public RecipeTypeDao(String nameDB) {
        super(nameDB);
    }

    @Override
    public List<String> getAllName() throws SQLException {
        String query = String.format("""
                SELECT Nom
                FROM TypePlat
                """, getUserID());
        return getListOfName(query);
    }

    @Override
    public void insert(String name) throws SQLException {
        String[] values = {"null", String.format("'%s'", name)};
        insert("TypePlat", values);
    }

    @Override
    public void update(String name){
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(String name){
        throw new UnsupportedOperationException();
    }
}
