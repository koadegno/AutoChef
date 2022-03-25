package ulb.infof307.g01.db;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeTypeDao extends Database implements Dao<String>{
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
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable("TypePlat",null);
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
