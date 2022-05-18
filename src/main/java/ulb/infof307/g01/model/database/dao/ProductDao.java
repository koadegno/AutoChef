package ulb.infof307.g01.model.database.dao;

import org.jetbrains.annotations.Nullable;
import ulb.infof307.g01.model.database.Database;
import ulb.infof307.g01.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les Ingredients
 */
public class ProductDao extends Database implements Dao<Product> {
    private static final String TABLE_NAME= "Ingredient";
    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public ProductDao(String nameDB) {
        super(nameDB);
    }

    @Override
    public List<String> getAllName() throws SQLException {
        String query = """
                SELECT nom
                FROM Ingredient 
                ORDER BY Nom ASC
                """;
        return getListOfName(query);
    }

    @Override
    public void insert(Product product) throws SQLException {
        int familyID = getIDFromName("FamilleAliment",product.getFamilyProduct());
        int unityID = getIDFromName("Unite",product.getNameUnity());
        String query = String.format("""
            INSERT INTO %s values (null,?, %s, %s);
            """,TABLE_NAME, familyID, unityID);
        sendInsertNameQueryWithPreparedStatement(product.getName(), query);
    }

    @Override
    public void update(Product product) throws SQLException { throw new UnsupportedOperationException(); }

    /**
     * @param name nom de l'ingredient que l'on cherche
     * @return premier objet Product trouvé
     */
    @Override
    public Product get(String name) throws SQLException {
        String query = String.format("""
                        SELECT * FROM %s
                        WHERE nom = ?
                """, TABLE_NAME);
        int nameIndexInPreparedStatement = 1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(nameIndexInPreparedStatement, name);
            ResultSet querySelectProduct =  sendQuery(statement);
            return getProductInformationInResultSet(querySelectProduct);
        }
    }

    @Nullable
    private Product getProductInformationInResultSet(ResultSet querySelectProduct) throws SQLException {
        if(!querySelectProduct.next()) return null;
        String nameProduct = querySelectProduct.getString("Nom");
        int familyProductID = querySelectProduct.getInt("FamilleAlimentID");
        int unityProductID = querySelectProduct.getInt("UniteID");
        String familyProduct = getNameFromID("FamilleAliment", familyProductID,"FamilleAlimentID");
        String unityProduct = getNameFromID("Unite", unityProductID,"UniteID");
        return new Product.ProductBuilder().withName(nameProduct).withQuantity(1).withFamilyProduct(familyProduct).withNameUnity(unityProduct).build();
    }
}
