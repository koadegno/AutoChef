package ulb.infof307.g01.db;

import com.esri.arcgisruntime.geometry.Point;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import ulb.infof307.g01.model.*;

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

    @Override // TODO Modifier pour avoir les coord aussi peut etre que t'as pas besoin
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable("Magasin","ORDER BY Nom ASC");
    }

    @Override
    public void insert(Shop shop) throws SQLException {
        //TODO rajouter les autres attributs
        String name = String.format("'%s'", shop.getName());
        String latitude = String.valueOf(shop.getCoordinateX());
        String longitude = String.valueOf(shop.getCoordinateY());

        String[] values = {"null", name,"null", latitude,longitude};
        insert("Magasin", values);
        String shopID = String.format("%d", getGeneratedID());

        for (Product product: shop) {
            String productID = String.format("%d", getIDFromName("Ingredient", product.getName(), "IngredientID"));
            String price =  String.format("%10f", product.getPrice());
            String[] productValues = {productID, String.valueOf(shop.getID()), price};
            insert("MagasinIngredient", productValues);
        }
    }

    @Override
    public void update(Shop shop) throws SQLException {
        //TODO
    }

    /**
     * trouve les magasins qui correspondent au nom
     * @param name le nom du magasin
     * @return
     * @throws SQLException
     */
    public List<Shop> getShops(String name) throws SQLException { // retourne tt les lidl de la map
        List<Shop> shops = getAllShops(name);

        for(Shop shop: shops){
            shop = fillShopWithProducts(shop);
        }
        return shops;

    }

    private Shop fillShopWithProducts(Shop shop) throws SQLException {
        ResultSet querySelectProductList = sendQuery(String.format("SELECT Ingredient.Nom,MagasinIngredient.prix\n" +
                "FROM MagasinIngredient as MI\n" +
                "INNER JOIN Magasin ON MI.MagasinID = Magasin.MagasinID\n" +
                "INNER JOIN Ingredient ON MI.IngredientID = Ingredient.IngredientID\n" +
                "WHERE MI.Nom = '%s', MI.longitude = %10f, MI.latitude = %10f ",shop.getName(),shop.getCoordinateY(),shop.getCoordinateX() ));
        querySelectProductList.next();
        String productName = querySelectProductList.getString("Nom");
        Double productPrice = querySelectProductList.getDouble("prix");
        shop.add(new Product(productName,productPrice));
        return shop;
    }

    private List<Shop> getAllShops(String name) throws SQLException {
        String nameConstraint = String.format("Nom = '%s'", name);
        ResultSet shopResultSet = select("Magasin", Collections.singletonList(nameConstraint),null);
        ArrayList<Shop> shopsList = new ArrayList<>();
        while (shopResultSet.next()){
            int shopID = shopResultSet.getInt("MagasinID");
            String shopName = shopResultSet.getString("Nom");
            Double shopX = shopResultSet.getDouble("latitude");
            Double shopY = shopResultSet.getDouble("longitude");
            Point shopPoint = new Point(shopX,shopY);
            shopsList.add(new Shop(shopID,shopName, shopPoint));
        }
        return shopsList;
    }

    @Override
    public Shop get(String name) throws SQLException , NotImplementedException{
        throw new NotImplementedException("Cette methode n'est pas implementer casse toi");

    }

    public Shop get(String name, Point coordinates) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        String nameConstraint = String.format("Nom = '%s' ", name);
        String latitudeConstraint = stringBuilder.append("latitude = ").append(coordinates.getX()).toString();
        stringBuilder = new StringBuilder();
        String longitudeConstraint = stringBuilder.append(" longitude = ").append(coordinates.getY()).toString();
        ArrayList<String> constraints = new ArrayList<String>();
        constraints.add(nameConstraint); constraints.add(latitudeConstraint); constraints.add(longitudeConstraint);

        ResultSet shopResultSet = select("Magasin", constraints,null);
        shopResultSet.next();
        int shopID = shopResultSet.getInt("MagasinID");
        String shopName = shopResultSet.getString("Nom");
        Double shopX = shopResultSet.getDouble("latitude");
        Double shopY = shopResultSet.getDouble("longitude");
        Point shopPoint = new Point(shopX,shopY);
        Shop shop = new Shop(shopID,shopName, shopPoint);
        shop = fillShopWithProducts(shop);

        return shop;
    }
}
