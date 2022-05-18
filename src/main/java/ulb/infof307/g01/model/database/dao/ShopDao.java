package ulb.infof307.g01.model.database.dao;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import org.jetbrains.annotations.NotNull;
import ulb.infof307.g01.model.ShoppingList;
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
    public static final int SHOP_ADDRESS_INDEX = 3;
    public static final int SHOP_LONGITUDE_INDEX = 5;
    public static final int SHOP_LATITUDE_INDEX = 4;
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
                """, Configuration.getCurrent().getCurrentUser().getId());
        return getListOfName(query);
    }

    /**
     * ajout un magasin a la base de donnée
     * @param shop l'objet magasin a ajouté
     * @throws SQLException erreur liée à la base de donnée
     */
    @Override
    public void insert(Shop shop) throws SQLException {
        insertShop(shop);
        String shopID = String.valueOf(getGeneratedID());
        insertShopProducts(shop, shopID);
        String query = String.format("""
            INSERT INTO %s values (%s, %s);
            """,TABLE_USER_MAGASIN,getUserID(),shopID);
        try (PreparedStatement statement = connection.prepareStatement(String.valueOf(query));) {
            sendQueryUpdate(statement);
        }
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
     * récupérer tous les magasins
     * @return la liste de tous les magasins
     * @throws SQLException erreur au niveau de la requête SQL
     */
    public List<Shop> getAllShops() throws SQLException {
        String query = String.format("""
                        SELECT M.MagasinID, M.Nom, M.adresse, M.latitude, M.longitude
                        FROM Magasin as M
                        INNER JOIN UtilisateurMagasin ON UtilisateurMagasin.MagasinID = M.MagasinID
                        WHERE UtilisateurMagasin.UtilisateurID = %d""",
                Configuration.getCurrent().getCurrentUser().getId());
        return fillAllShopsWithProducts(getShopsList(query));
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
        int shopIndex = 0;
        int nameIndexInPreparedStatement = 1;
        String query = String.format("""
                        SELECT M.MagasinID, M.Nom, M.adresse, M.latitude, M.longitude
                        FROM Magasin as M
                        INNER JOIN UtilisateurMagasin ON UtilisateurMagasin.MagasinID = M.MagasinID
                        WHERE M.Nom = ? AND M.latitude = %s AND M.longitude = %s AND UtilisateurMagasin.UtilisateurID = %d""",
                coordinates.getX(), coordinates.getY(), getUserID());

        try (PreparedStatement statement = connection.prepareStatement(String.valueOf(query));) {
            statement.setString(nameIndexInPreparedStatement,name);
            List<Shop> shopsList = getShopsList(statement);
            if(shopsList.isEmpty())return null;
            return fillAllShopsWithProducts(shopsList).get(shopIndex);
        }
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

    /**
     * Rêquete pour avoir les magasins possendant toutes les ingredients d'une liste de course
     * @param shoppingList
     * @return Liste des magasion qui ont les ingredient de la liste de course
     * @throws SQLException
     */
    public List<Shop>  getShopWithProductList(ShoppingList shoppingList) throws SQLException {
        int shoppingListID = shoppingList.getId();
        String query = String.format("""
                                        SELECT M.MagasinID, M.Nom, M.adresse, M.latitude, M.longitude
                                        FROM  Magasin as M
                                        INNER JOIN MagasinIngredient MI on MI.MagasinID = M.MagasinID
                                        INNER JOIN ListeCourseIngredient LCI on MI.IngredientID = LCI.IngredientID
                                        WHERE LCI.ListeCourseID = %d
                                        GROUP BY MI.MagasinID
                                        HAVING count(*) = (SELECT Count(*) FROM ListeCourseIngredient LCI2 WHERE LCI2.ListeCourseID = %d)
                                        """, shoppingListID, shoppingListID);
        return fillAllShopsWithProducts(getShopsList(query));
    }

    /**
     *
     * @param shoppingList
     * @return Une liste de magasins contenant les ingredients de la liste de course au prix le plus bas
     * @throws SQLException
     */
    public List<Shop>  getShopWithMinPriceForProductList(ShoppingList shoppingList) throws SQLException {
        int shoppingListID = shoppingList.getId();
        String query = String.format("""
                SELECT resultats.MagasinID , resultats.Nom, resultats.adresse, resultats.latitude, resultats.longitude
                            FROM
                            (
                                SELECT sommes.MagasinID , sommes.Nom,  sommes.adresse, sommes.latitude, sommes.longitude, MIN(sommes.PrixTotal)
                                FROM
                                    (
                                        SELECT M.MagasinID, M.Nom, M.adresse, M.latitude, M.longitude, SUM(MI.prix) as PrixTotal
                                        FROM  Magasin as M
                                                  INNER JOIN MagasinIngredient MI on MI.MagasinID = M.MagasinID
                                                  INNER JOIN ListeCourseIngredient LCI on MI.IngredientID = LCI.IngredientID
                                        WHERE LCI.ListeCourseID = %d
                                        GROUP BY MI.MagasinID
                                        HAVING count(*) = (SELECT Count(*) FROM ListeCourseIngredient LCI2 WHERE LCI2.ListeCourseID = %d)
                                    ) sommes
                            ) resultats
                                        """, shoppingListID, shoppingListID);

        return fillAllShopsWithProducts(getShopsList(query));

    }

    /**
     *
     * @param shoppingList
     * @param position : la position de départ
     * @return Liste des magasins les plus proches contenant les ingredients d'une liste de courses
     * @throws SQLException
     */
    public List<Shop>  getNearestShopsWithProductList(ShoppingList shoppingList, Point position) throws SQLException {
        int shoppingListID = shoppingList.getId();
        String query = String.format("""
                        SELECT resultats.MagasinID , resultats.Nom, resultats.adresse, resultats.latitude, resultats.longitude
                        FROM
                        (
                            SELECT sommes.MagasinID , sommes.Nom, sommes.adresse, sommes.latitude, sommes.longitude, MIN(sommes.distance)
                            FROM
                                
                                (
                                    SELECT M.MagasinID, M.Nom, M.adresse, M.latitude, M.longitude, SQRT(POWER(M.latitude-%s, 2) + POWER(M.longitude-%s,2)) as distance
                                    FROM  Magasin as M
                                              INNER JOIN MagasinIngredient MI on MI.MagasinID = M.MagasinID
                                              INNER JOIN ListeCourseIngredient LCI on MI.IngredientID = LCI.IngredientID
                                    WHERE LCI.ListeCourseID = %d
                                    GROUP BY MI.MagasinID
                                    HAVING count(*) = (SELECT Count(*) FROM ListeCourseIngredient LCI2 WHERE LCI2.ListeCourseID = %d)
                                ) sommes
                        ) resultats
                """, position.getX(),position.getY(),shoppingListID, shoppingListID);
        return fillAllShopsWithProducts(getShopsList(query));
    }

    /**
     *
     * @param shop : le magasins pour lequel on souhaite connaitre le prix d'une liste de produitss
     * @param shoppingList
     * @return Le prix totale d'une liste d'ingredient dans un magasin
     * @throws SQLException
     */
    public double getShoppingListPriceInShop(Shop shop, ShoppingList shoppingList)throws SQLException{
        double totalPrice = -1; //error value
        final int indexSum = 1;
        String query = String.format("""
                SELECT SUM(MI.prix)
                FROM  MagasinIngredient as MI
                INNER JOIN ListeCourseIngredient LCI on MI.IngredientID = LCI.IngredientID
                WHERE LCI.ListeCourseID = %d and MI.MagasinID = %d
                """, shoppingList.getId(), shop.getID());
        try (ResultSet querySelectShop = sendQuery(query)){
            if(querySelectShop.next()) {
                totalPrice = Math.round(querySelectShop.getDouble(indexSum));
            }

        }
        return totalPrice;
    }

    // TOOLS
    @NotNull
    private List<Shop> fillAllShopsWithProducts(List<Shop> shops) throws SQLException {
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
                Configuration.getCurrent().getCurrentUser().getId());

        try (ResultSet querySelectProductList = sendQuery(query)) {
            while (querySelectProductList != null && querySelectProductList.next()) {
                String productName = querySelectProductList.getString("Nom");
                double productPrice = querySelectProductList.getDouble("prix");
                shop.add(new Product.ProductBuilder().withName(productName).withPrice(productPrice).build());
            }
        }
    }

    private List<Shop> getShopsList(ResultSet resultSet ){
        List<Shop> shopsList =  new ArrayList<>();
        try {
            while (resultSet.next()) {
                int shopID = resultSet.getInt(SHOP_ID_INDEX);
                String shopName = resultSet.getString(SHOP_NAME_INDEX);
                String shopAddress = resultSet.getString(SHOP_ADDRESS_INDEX);
                double shopX = resultSet.getDouble(SHOP_LATITUDE_INDEX);
                double shopY = resultSet.getDouble(SHOP_LONGITUDE_INDEX);
                Point shopPoint = new Point(shopX, shopY, SpatialReferences.getWebMercator());
                Shop shop =  new Shop(shopID, shopName,shopAddress, shopPoint);
                shopsList.add(shop);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return shopsList;
    }
    @NotNull
    private List<Shop> getShopsList(String query) throws SQLException {
        try (ResultSet querySelectShop = sendQuery(query)){
            return getShopsList(querySelectShop);
        }
    }
    @NotNull
    private List<Shop> getShopsList(PreparedStatement statement) throws SQLException {
        try (ResultSet querySelectShop = sendQuery(statement)){
            return getShopsList(querySelectShop);
        }
    }

    /**
     * Insert les produits d'un magasin dans la table MagasinIngredient
     * @param shop
     * @param shopID
     * @throws SQLException
     */
    private void insertShopProducts(Shop shop, String shopID) throws SQLException {
        for (Product product: shop) { //Tous les produits ont déja été vérifié à l'insertion
            String productID = String.format("%d", getIDFromName("Ingredient", product.getName(), "IngredientID"));
            String price =  String.valueOf(product.getPrice());
            String query = String.format("""
            INSERT INTO %s values (%s, %s,%s);
            """,TABLE_SHOP_PRODUCT,shopID,productID,price);
            try (PreparedStatement statement = connection.prepareStatement(String.valueOf(query));) {
                sendQueryUpdate(statement);
            }
        }
    }

    /**
     * Insert un magasin dans la table magasin
     * @param shop
     * @throws SQLException
     */
    private void insertShop(Shop shop) throws SQLException {
        String name =  shop.getName();
        String address = shop.getAddress();
        double latitude = shop.getCoordinateX();
        double longitude = shop.getCoordinateY();
        String query = String.format("""
            INSERT INTO %s values (null,?,'%s',%s,%s );
        """,MAGASIN_TABLE_NAME, address,longitude,latitude); //Seulement le nom vient de l'utilisateur
        sendInsertNameQueryWithPreparedStatement(name,query);
    }
}
