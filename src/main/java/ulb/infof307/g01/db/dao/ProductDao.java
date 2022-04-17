package ulb.infof307.g01.db.dao;

import ulb.infof307.g01.db.Database;
import ulb.infof307.g01.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe d'accès à la base de données pour les données concernant les Ingredients
 */
public class ProductDao extends Database implements Dao<Product> {

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
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable("Ingredient","ORDER BY Nom ASC");
    }

    @Override
    public void insert(Product product) throws SQLException {
        int familyID = getIDFromName("FamilleAliment",product.getFamillyProduct(),"FamilleAlimentID");
        int unityID = getIDFromName("Unite",product.getNameUnity(),"UniteID");
        String stringFamilyID = String.format("%d", familyID);
        String stringUnityID = String.format("%d",unityID);
        String stringName = String.format("'%s'",product.getName());
        String[] values = {"null",stringName, stringFamilyID,stringUnityID};
        insert("Ingredient",values);
    }

    @Override
    public void update(Product product) throws SQLException { throw new UnsupportedOperationException(); }

    /**
     * @param name nom de l'ingredient que l'on cherche
     * @return premier objet Product trouvé
     */
    @Override
    public Product get(String name) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        constraint.add(String.format("%s = '%s'","Nom",name));
        PreparedStatement statement  = select("Ingredient",constraint,null);
        ResultSet querySelectProduct = sendQuery(statement);
        if(!querySelectProduct.next())return null;


        String nameProduct = querySelectProduct.getString("Nom");
        int familyProductID = querySelectProduct.getInt("FamilleAlimentID");
        int unityProductID = querySelectProduct.getInt("UniteID");
        String familyProduct = getNameFromID("FamilleAliment", familyProductID,"FamilleAlimentID");
        String unityProduct = getNameFromID("Unite", unityProductID,"UniteID");

        return new Product(nameProduct,1,unityProduct,familyProduct);
    }
}
