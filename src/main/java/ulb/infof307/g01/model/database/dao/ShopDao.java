package ulb.infof307.g01.model.database.dao;

import com.esri.arcgisruntime.geometry.Point;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.Database;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les magasins
 */
public class ShopDao extends Database implements Dao<Shop> {

    public static final int SHOP_ID_INDEX = 1;
    public static final int SHOP_NAME_INDEX = 2;
    public static final int SHOP_LONGITUDE_INDEX = 4;
    public static final int SHOP_LATITUDE_INDEX = 3;
    public static final String MAGASIN_TABLE_NAME = "Magasin";
    public static final String TABLE_SHOP_PRODUCT = "MagasinIngredient";
    public static final String TABLE_USER_MAGASIN = "UtilisateurMagasin";

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'on veut charger/créer.
     */
    public ShopDao(String nameDB) {
        super(nameDB);
    }

    /**
     * retourne tous les noms de magasins que l'utilisateur a enregistré
     * @return une liste avec tous les noms de magasin
     * @throws SQLException erreur liée a la base de donnée
     */
    @Override
    public List<String> getAllName() throws SQLException {

        String query = String.format("""
                SELECT R.Nom
                FROM Magasin as R
                INNER JOIN UtilisateurMagasin ON R.MagasinID = UtilisateurMagasin.MagasinID
                WHERE UtilisateurMagasin.UtilisateurID = %d
                ORDER BY Nom ASC
                """, Configuration.getCurrent().getCurrentUser().getID());
        ResultSet queryAllName = sendQuery(query);
        List<String> nameList = new ArrayList<>();
        while(queryAllName.next()){
            nameList.add(queryAllName.getString(1));
        }

        return nameList;
    }

    /**
     * ajout un magasin a la base de donnée
     * @param shop l'objet magasin a ajouté
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public void insert(Shop shop) throws SQLException {
        String name = String.format("'%s'", shop.getName());
        String latitude = String.valueOf(shop.getCoordinateX());
        String longitude = String.valueOf(shop.getCoordinateY());

        String[] values = {"null", name,"null", longitude,latitude};
        insert(MAGASIN_TABLE_NAME, values);

        String shopID = String.valueOf(getGeneratedID());

        for (Product product: shop) {
            String productID = String.format("%d", getIDFromName("Ingredient", product.getName(), "IngredientID"));
            String price =  String.valueOf(product.getPrice());
            String[] productValues = {shopID,productID,price};
            insert(TABLE_SHOP_PRODUCT, productValues);
        }
        // ajout dans la table utilisateur
        String userID = String.valueOf(Configuration.getCurrent().getCurrentUser().getID());
        String[] userShopValues = {userID,shopID};
        insert("UtilisateurMagasin", userShopValues);
    }

    /**
     * Methode rustique de mise a jour supprimer l'ancienne valeur et
     * ajout la nouvelle modifié
     * @param shop le magasin à mettre a jour
     * @throws SQLException erreur liée à la base de donnée
     */
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
                INNER JOIN UtilisateurMagasin ON UtilisateurMagasin.MagasinID = MI.MagasinID
                WHERE MI.MagasinID = %d AND UtilisateurMagasin.UtilisateurID = %d""",
                shop.getID(),
                Configuration.getCurrent().getCurrentUser().getID());

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
        ResultSet querySelectShop = sendQuery(String.format("""
            SELECT M.MagasinID, M.Nom, M.latitude, M.longitude
            FROM Magasin as M
            INNER JOIN UtilisateurMagasin ON UtilisateurMagasin.MagasinID = M.MagasinID
            WHERE UtilisateurMagasin.UtilisateurID = %d""",
            Configuration.getCurrent().getCurrentUser().getID()));

        ArrayList<Shop> shopsList = new ArrayList<>();
        while (querySelectShop.next()){
            int shopID = querySelectShop.getInt(SHOP_ID_INDEX);
            String shopName = querySelectShop.getString(SHOP_NAME_INDEX);
            double shopX = querySelectShop.getDouble(SHOP_LATITUDE_INDEX);
            double shopY = querySelectShop.getDouble(SHOP_LONGITUDE_INDEX);
            Point shopPoint = new Point(shopX,shopY);
            shopsList.add(new Shop(shopID,shopName, shopPoint));
        }
        return shopsList;
    }

    @Override
    public Shop get(String name)throws IllegalCallerException{
        throw new IllegalCallerException("Cette methode n'est pas implementé");
    }

    /**
     * Recupère un magasin en fonction de son nom et de ces coordonnées
     * @param name le nom du magasin
     * @param coordinates les coordonnées du magasin
     * @return le magasin qui correspond
     * @throws SQLException erreur avec la requête SQL
     */
    public Shop get(String name, Point coordinates) throws SQLException {

        ResultSet querySelectShop = sendQuery(String.format("""
                SELECT M.MagasinID
                FROM Magasin as M
                INNER JOIN UtilisateurMagasin ON UtilisateurMagasin.MagasinID = M.MagasinID
                WHERE M.Nom = '%s' AND M.latitude = %s AND M.longitude = %s AND UtilisateurMagasin.UtilisateurID = %d""",
                name,String.valueOf(coordinates.getX()), String.valueOf(coordinates.getY()),Configuration.getCurrent().getCurrentUser().getID()));

        if(querySelectShop.next()){
            int shopID = querySelectShop.getInt(SHOP_ID_INDEX);
            Shop shop = new Shop(shopID,name, coordinates);
            fillShopWithProducts(shop);
            return shop;
        }

        return null;
    }

    /**
     * Supprime un magasin de la base de donnée
     * @param shop le magasin a supprimé
     * @throws SQLException erreur liée à la base de donnée
     */
    public void delete(Shop shop) throws SQLException {
        String[] constraint = {"MagasinID = "+ shop.getID()};
        delete(TABLE_SHOP_PRODUCT, List.of(constraint));
        delete(TABLE_USER_MAGASIN, List.of(constraint));
        delete(MAGASIN_TABLE_NAME,List.of(constraint));

    }
}
