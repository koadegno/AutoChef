package ulb.infof307.g01.db;

import com.esri.arcgisruntime.geometry.Point;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.util.NotImplemented;
import ulb.infof307.g01.model.*;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopDao extends Database implements Dao<Shop>{

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public ShopDao(String nameDB) {
        super(nameDB);
    }

    @Override
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable("Magasin","ORDER BY Nom ASC");
    }

    @Override
    public void insert(Shop shop) throws SQLException {
        //TODO rajouter les autres attributs
        String name = String.format("'%s'", shop.getName());
        String latitude = String.valueOf(shop.getCoordinateX());
        String longitude = String.valueOf(shop.getCoordinateY());

        String[] values = {"null", name,"null", longitude,latitude};
        insert("Magasin", values);
        String shopID = String.format("%d", getGeneratedID());

        for (Product product: shop) {
            String productID = String.format("%d", getIDFromName("Ingredient", product.getName(), "IngredientID"));
            String price =  String.valueOf(product.getPrice());
            String[] productValues = {shopID,productID,price};
            insert("MagasinIngredient", productValues);
        }
    }

    @Override
    public void update(Shop shop) throws SQLException {
        delete(shop);
        insert(shop);
    }

    /**
     * trouve les magasins qui correspondent au nom
     * @return la liste de tous les magasins
     * @throws SQLException erreur avec la requête SQL
     */
    public List<Shop> getShops() throws SQLException {
        // TODO Attention je pense qu'on a pas besoin de cette methode
        List<Shop> shops = getAllShops();

        for(Shop shop: shops){
            shop = fillShopWithProducts(shop);
        }
        return shops;
    }

    /**
     * Remplie un magasin avec les produits qui lui sont associés
     * @param shop le magasin qui doit etre remplie
     * @return le magasin remplie
     * @throws SQLException erreur au niveau de la requête SQL
     */
    private Shop fillShopWithProducts(Shop shop) throws SQLException {
        ResultSet querySelectProductList = sendQuery(String.format("SELECT Ingredient.Nom,MI.prix\n" +
                "FROM MagasinIngredient as MI\n" +
                "INNER JOIN Magasin ON MI.MagasinID = Magasin.MagasinID\n" +
                "INNER JOIN Ingredient ON MI.IngredientID = Ingredient.IngredientID\n" +
                "WHERE MI.MagasinID = '%d' AND ",shop.getID()));
        if(querySelectProductList != null &&querySelectProductList.next()){
            String productName = querySelectProductList.getString("Nom");
            double productPrice = querySelectProductList.getDouble("prix");
            shop.add(new Product(productName,productPrice));
        }
        return shop;
    }

    /**
     * récupérer tous les magasins qui contiennent le nom
     * @return la liste de tous les magasins
     * @throws SQLException erreur au niveau de la requête SQL
     */
    private List<Shop> getAllShops() throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        ResultSet shopResultSet = select("Magasin", new ArrayList<>(),null);
        ArrayList<Shop> shopsList = new ArrayList<>();
        while (shopResultSet.next()){
            int shopID = shopResultSet.getInt("MagasinID");
            String shopName = shopResultSet.getString("Nom");
            double shopX = shopResultSet.getDouble("latitude");
            double shopY = shopResultSet.getDouble("longitude");
            Point shopPoint = new Point(shopX,shopY);
            shopsList.add(new Shop(shopID,shopName, shopPoint));
        }
        return shopsList;
    }

    @Override
    @NotImplemented
    /**
     * Methode inutilisé
     */
    public Shop get(String name) throws SQLException , NotImplementedException{
        throw new NotImplementedException("Cette methode n'est pas implementé");

    }

    public Shop get(String name, Point coordinates) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        String nameConstraint = String.format("Nom = '%s' ", name);
        String latitudeConstraint = stringBuilder.append("latitude = ").append(coordinates.getX()).toString();
        stringBuilder = new StringBuilder();
        String longitudeConstraint = stringBuilder.append(" longitude = ").append(coordinates.getY()).toString();
        ArrayList<String> constraints = new ArrayList<>();
        constraints.add(nameConstraint); constraints.add(latitudeConstraint); constraints.add(longitudeConstraint);

        ResultSet shopResultSet = select("Magasin", constraints,null);
        if(shopResultSet.next()){
            int shopID = shopResultSet.getInt(1);
            String shopName = shopResultSet.getString(2);
            // TODO L'adresse plus tard
            double shopY = shopResultSet.getDouble(4);
            double shopX = shopResultSet.getDouble(5);
            Point shopPoint = new Point(shopX,shopY);
            Shop shop = new Shop(shopID,shopName, shopPoint);
            shop = fillShopWithProducts(shop);
            return shop;
        }

        return null;
    }

    public void delete(Shop shop){

        sendQuery(String.format("DELETE FROM MagasinIngredient as MI\n" +
                "WHERE MI.MagasinID = %d",shop.getID()));

        sendQuery(String.format("DELETE FROM Magasin as M\n" +
                "WHERE M.MagasinID = %d",shop.getID()));
    }
}
