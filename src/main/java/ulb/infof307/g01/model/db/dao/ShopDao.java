package ulb.infof307.g01.model.db.dao;

import com.esri.arcgisruntime.geometry.Point;
import ulb.infof307.g01.model.db.Database;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDao extends Database implements Dao<Shop> {

    public static final int SHOP_ID_INDEX = 1;
    public static final int SHOP_NAME_INDEX = 2;
    public static final int SHOP_LONGITUDE_INDEX = 4;
    public static final int SHOP_LATITUDE_INDEX = 5;

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'on veut charger/créer.
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
     * tous les magasins stocker
     * @return la liste de tous les magasins
     * @throws SQLException erreur avec la requête SQL
     */
    public List<Shop> getShops() throws SQLException {
        List<Shop> shops = getAllShops();
        for(Shop shop: shops){
            fillShopWithProducts(shop);
        }
        return shops;
    }

    /**
     * Remplie un magasin avec les produits qui lui sont associés
     * @param shop le magasin qui doit etre remplie
     * @throws SQLException erreur au niveau de la requête SQL
     */
    private void fillShopWithProducts(Shop shop) throws SQLException {
        String query = String.format("""
                SELECT Ingredient.Nom,MI.prix
                FROM MagasinIngredient as MI
                INNER JOIN Magasin ON MI.MagasinID = Magasin.MagasinID
                INNER JOIN Ingredient ON MI.IngredientID = Ingredient.IngredientID
                WHERE MI.MagasinID = %d""",shop.getID());
        ResultSet querySelectProductList = sendQuery(query);
        while(querySelectProductList != null &&querySelectProductList.next()){
            String productName = querySelectProductList.getString("Nom");
            double productPrice = querySelectProductList.getDouble("prix");
            shop.add(new Product(productName,productPrice));
        }
    }

    /**
     * récupérer tous les magasins
     * @return la liste de tous les magasins
     * @throws SQLException erreur au niveau de la requête SQL
     */
    private List<Shop> getAllShops() throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        PreparedStatement statement =  select("Magasin", new ArrayList<>(),null);
        ResultSet  shopResultSet = sendQuery(statement);
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
    public Shop get(String name) throws SQLException , IllegalCallerException{
        throw new IllegalCallerException("Cette methode n'est pas implementé"); // TODO implémenter
    }

    /**
     * Recupère un magasin en fonction de son nom et de ces coordonnées
     * @param name le nom du magasin
     * @param coordinates les coordonnées du magasin
     * @return le magasin qui correspond
     * @throws SQLException erreur avec la requête SQL
     */
    public Shop get(String name, Point coordinates) throws SQLException {
        // construction des contraintes
        StringBuilder stringBuilder = new StringBuilder();
        String nameConstraint = String.format("Nom = '%s' ", name);
        String latitudeConstraint = stringBuilder.append("latitude = ").append(coordinates.getX()).toString();
        stringBuilder = new StringBuilder();
        String longitudeConstraint = stringBuilder.append(" longitude = ").append(coordinates.getY()).toString();
        ArrayList<String> constraints = new ArrayList<>();
        // ajout des contraintes
        constraints.add(nameConstraint); constraints.add(latitudeConstraint); constraints.add(longitudeConstraint);

        PreparedStatement statement = select("Magasin", constraints,null);
        ResultSet shopResultSet = sendQuery(statement);
        if(shopResultSet.next()){
            int shopID = shopResultSet.getInt(SHOP_ID_INDEX);
            String shopName = shopResultSet.getString(SHOP_NAME_INDEX);
            // TODO L'adresse plus tard
            double shopY = shopResultSet.getDouble(SHOP_LONGITUDE_INDEX);
            double shopX = shopResultSet.getDouble(SHOP_LATITUDE_INDEX);
            Point shopPoint = new Point(shopX,shopY);
            Shop shop = new Shop(shopID,shopName, shopPoint);
            fillShopWithProducts(shop);
            return shop;
        }

        return null;
    }

    public void delete(Shop shop) throws SQLException {
        String[] constraint = {"MagasinID = "+ shop.getID()};
        delete("MagasinIngredient", List.of(constraint));
        delete("Magasin",List.of(constraint));

    }
}
