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

    /**
     * Insert l'objet de type T dans la base de donnée
     * @param t un objet étant dans la classe Model ou représentant
     *          une table de la base de donnée
     * @throws SQLException erreur avec la base de donnée
     */
    void insert(T t) throws SQLException;

    /**
     * mettre a jour l'objet de type T dans la base de donnée
     * @param t un objet étant dans la classe Model ou représentant
     *        une table de la base de donnée
     * @throws SQLException erreur avec la base de donnée
     */
    void update(T t) throws SQLException;
    // Optional<T> maybe

    /**
     * récupérer l'objet ayant name dans une de ces colonnes, généralement name est la clé
     * @param name clé ou identifiant unique représentant un élément de la base de donnée
     * @return un objet de type T  étant dans la classe Model ou représentant
     *         une table de la base de donnée
     * @throws SQLException erreur avec la base de donnée
     */
    T get(String name) throws SQLException;

}
