package ulb.infof307.g01.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;


public interface Dao<T> {
    ArrayList<String> getAllName() throws SQLException;
    void insert(T t) throws SQLException;
    void update(T t) throws SQLException;
    // Optional<T> maybe
    T get(String name) throws SQLException;

}
