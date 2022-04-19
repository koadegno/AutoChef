package ulb.infof307.g01.model.database.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface d'accès aux méthodes de la base de données
 * Contient les méthodes générales de lecture et d'écriture
 * @param <T>
 */
public interface Dao<T> {
    List<String> getAllName() throws SQLException;
    void insert(T t) throws SQLException;
    void update(T t) throws SQLException;
    // Optional<T> maybe
    T get(String name) throws SQLException;

}
