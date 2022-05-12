package ulb.infof307.g01.model.database;

import com.esri.arcgisruntime.geometry.Point;
import org.junit.jupiter.api.*;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test du DAO du magasin
 */
class TestShopDao {
    static  private final String MY_SHOPPING_LIST_FOR_TEST = "myShoppingForTest";
    static private final String FRUIT = "Fruit";
    static private final String GRAM = "g";
    static private final Product PEACH = new Product("peche", 1, GRAM, FRUIT);
    static private final Product STRAWBERRY = new Product( "fraise", 1, GRAM, FRUIT);
    static private final Product MANGO = new Product( "mango", 1, GRAM, FRUIT);
    static private final Product MELON = new Product( "melon", 1, GRAM, FRUIT);
    static private final Shop ALDI_SHOP = new Shop("1 aldi",new Point(0,0));
    static private final Shop LIDL_SHOP = new Shop("aldi Namur",new Point(0,1));
    static private final Shop ALDI_SHOP2 = new Shop("Namur2",new Point(0,3));
    static private final Shop CARREFOUR_ANVERS2 = new Shop("Carrefour Anvers2",new Point(0,17));
    static private final Shop ALDI_RUE_NEUVE = new Shop("1 aldi Rue neuve",new Point(0,2));
    static private final Shop CARREFOUR_ANVERS = new Shop(3,"Carrefour Anvers", new Point(50,30));

    static private final String DATABASE_NAME = "test.sqlite";
    static ShoppingList myShoppingList;


    @BeforeEach
    public void initConfig() throws SQLException {
        Configuration.getCurrent().setDatabase(DATABASE_NAME);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getProductUnityDao().insert(GRAM);
        Configuration.getCurrent().getProductFamilyDao().insert(FRUIT);
        Configuration.getCurrent().getProductDao().insert(PEACH);
        Configuration.getCurrent().getProductDao().insert(STRAWBERRY);
        Configuration.getCurrent().getProductDao().insert(MANGO);
        Configuration.getCurrent().getProductDao().insert(MELON);
        Configuration.getCurrent().getShopDao().insert(ALDI_SHOP);
        Configuration.getCurrent().getShopDao().insert(ALDI_RUE_NEUVE);
        Configuration.getCurrent().getShopDao().insert(CARREFOUR_ANVERS);
        Configuration.getCurrent().getShopDao().insert(ALDI_SHOP2);
        Configuration.getCurrent().getShopDao().insert(CARREFOUR_ANVERS2);
        //ShoppingList
        myShoppingList = new ShoppingList(MY_SHOPPING_LIST_FOR_TEST);
        myShoppingList.add(Configuration.getCurrent().getProductDao().get(MANGO.getName()));
        myShoppingList.add(Configuration.getCurrent().getProductDao().get(MELON.getName()));
        Configuration.getCurrent().getShoppingListDao().insert(myShoppingList);
        myShoppingList = Configuration.getCurrent().getShoppingListDao().get(myShoppingList.getName());

    }

    @AfterEach
    public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(DATABASE_NAME));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> allShopName = Configuration.getCurrent().getShopDao().getAllName();
        assertEquals(ALDI_SHOP.getName(),allShopName.get(0));
        assertEquals(ALDI_RUE_NEUVE.getName(),allShopName.get(1));
    }

    @Test
    void testDelete() throws SQLException{

        Configuration.getCurrent().getShopDao().delete(CARREFOUR_ANVERS);
        List<Shop> shopList = Configuration.getCurrent().getShopDao().getShops();
        assertNotEquals(shopList.get(shopList.size()-1), CARREFOUR_ANVERS);
    }

    @Test
    void testInsert() throws SQLException {
        Point point = new Point(LIDL_SHOP.getCoordinateX(), LIDL_SHOP.getCoordinateY());
        Configuration.getCurrent().getShopDao().insert(LIDL_SHOP);
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(LIDL_SHOP.getName(), point);

        assertNotNull(shopInserted);
        assertEquals(LIDL_SHOP,shopInserted);
    }

    @Test
    void testUpdate() throws SQLException {
        Point coordinate = new Point(1,1);
        Shop aldiBruxellesShop = new Shop(2,"aldi Bruxelles",coordinate);
        Configuration.getCurrent().getShopDao().insert(aldiBruxellesShop);
        aldiBruxellesShop = new Shop(2,"aldi Zaventem",coordinate);
        Configuration.getCurrent().getShopDao().update(aldiBruxellesShop);
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(aldiBruxellesShop.getName(),aldiBruxellesShop.getCoordinate());

        assertEquals(aldiBruxellesShop,shopInserted);

    }

    @Test
    void testGetShops() throws SQLException {

        List<Shop> shopList = Configuration.getCurrent().getShopDao().getShops();
        assertEquals(shopList.get(0), ALDI_SHOP);
        assertEquals(shopList.get(1), ALDI_RUE_NEUVE);

    }

    @Test
    void testGet() throws SQLException {
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(ALDI_SHOP.getName(), ALDI_SHOP.getCoordinate());

        assertEquals(ALDI_SHOP,shopInserted);

    }

    @Test
    void testFillShopWithProduct() throws SQLException{

        Shop shopToTest = Configuration.getCurrent().getShopDao().get(ALDI_SHOP.getName(),ALDI_SHOP.getCoordinate());

        assertEquals(ALDI_SHOP.size(),shopToTest.size());

    }

    @Test
    void getShopWithProductList() throws SQLException {
        int nbShops = 2;
        Shop aldi = Configuration.getCurrent().getShopDao().get(ALDI_SHOP2.getName(), ALDI_SHOP2.getCoordinate());
        Shop carrefour = Configuration.getCurrent().getShopDao().get(CARREFOUR_ANVERS2.getName(), CARREFOUR_ANVERS2.getCoordinate());
        aldi.add(Configuration.getCurrent().getProductDao().get(MANGO.getName()));
        aldi.add(Configuration.getCurrent().getProductDao().get(MELON.getName()));
        carrefour.add(Configuration.getCurrent().getProductDao().get(MANGO.getName()));
        carrefour.add(Configuration.getCurrent().getProductDao().get(MELON.getName()));
        Configuration.getCurrent().getShopDao().update(aldi);
        Configuration.getCurrent().getShopDao().update(carrefour);
        List<Shop> allShopsWithShoppingList = Configuration.getCurrent().getShopDao().getShopWithProductList(myShoppingList);
        assertEquals(allShopsWithShoppingList.size(), nbShops);
        assertTrue(allShopsWithShoppingList.contains(aldi));
        assertTrue(allShopsWithShoppingList.contains(carrefour));

    }

    @Test
    void getShopWithMinPriceForProductList() throws SQLException {
        int nbShopWithMinPrice = 1;
        double carrefourMangoPrice = 2;
        double aldiMangoPrice = 24;
        double carrefourMelonPrice = 4;
        double aldiMelonPrice = 6;
        //products
        Product mango = Configuration.getCurrent().getProductDao().get(MANGO.getName());
        Product melon = Configuration.getCurrent().getProductDao().get(MELON.getName());

        //shops
        Shop aldi = Configuration.getCurrent().getShopDao().get(ALDI_SHOP2.getName(), ALDI_SHOP2.getCoordinate());
        Shop carrefour = Configuration.getCurrent().getShopDao().get(CARREFOUR_ANVERS2.getName(), CARREFOUR_ANVERS2.getCoordinate());
        aldi.remove(mango);
        aldi.remove(melon);
        carrefour.remove(mango);
        carrefour.remove(melon);
        mango.setPrice(aldiMangoPrice);
        melon.setPrice(aldiMelonPrice);
        aldi.add(mango);
        aldi.add(melon);
        Configuration.getCurrent().getShopDao().update(aldi);
        mango.setPrice(carrefourMangoPrice);
        melon.setPrice(carrefourMelonPrice);
        carrefour.add(mango);
        carrefour.add(melon);
        Configuration.getCurrent().getShopDao().update(carrefour);
        //test
        List<Shop> allShopsWithMinPriceShoppingList = Configuration.getCurrent().getShopDao().getShopWithMinPriceForProductList(myShoppingList);
        assertEquals(allShopsWithMinPriceShoppingList.size(), nbShopWithMinPrice);
        assertTrue(allShopsWithMinPriceShoppingList.contains(carrefour));
    }

    @Test
    void getNearestShopsWithProductList() throws SQLException {
        int nbShops = 1;
        Point position = new Point(0,3); //same position as aldi
        Shop aldi = Configuration.getCurrent().getShopDao().get(ALDI_SHOP2.getName(), ALDI_SHOP2.getCoordinate());
        Shop carrefour = Configuration.getCurrent().getShopDao().get(CARREFOUR_ANVERS2.getName(), CARREFOUR_ANVERS2.getCoordinate());
        aldi.add(Configuration.getCurrent().getProductDao().get(MANGO.getName()));
        aldi.add(Configuration.getCurrent().getProductDao().get(MELON.getName()));
        carrefour.add(Configuration.getCurrent().getProductDao().get(MANGO.getName()));
        carrefour.add(Configuration.getCurrent().getProductDao().get(MELON.getName()));
        Configuration.getCurrent().getShopDao().update(aldi);
        Configuration.getCurrent().getShopDao().update(carrefour);
        List<Shop> nearestShopsWithShoppingList = Configuration.getCurrent().getShopDao().getNearestShopsWithProductList(myShoppingList, position);
        assertEquals(nearestShopsWithShoppingList.size(), nbShops);
        assertTrue(nearestShopsWithShoppingList.contains(aldi));
    }

    @Test
    void getShoppingListPriceInShop() throws SQLException {
        double carrefourMangoPrice = 2;
        double carrefourMelonPrice = 4;
        //products
        Product mango = Configuration.getCurrent().getProductDao().get(MANGO.getName());
        Product melon = Configuration.getCurrent().getProductDao().get(MELON.getName());
        //shops
        Shop carrefour = Configuration.getCurrent().getShopDao().get(CARREFOUR_ANVERS2.getName(), CARREFOUR_ANVERS2.getCoordinate());
        mango.setPrice(carrefourMangoPrice);
        melon.setPrice(carrefourMelonPrice);
        carrefour.add(mango);
        carrefour.add(melon);
        Configuration.getCurrent().getShopDao().update(carrefour);
        carrefour = Configuration.getCurrent().getShopDao().get(CARREFOUR_ANVERS2.getName(), CARREFOUR_ANVERS2.getCoordinate());
        Double price = Configuration.getCurrent().getShopDao().getShoppingListPriceInShop(carrefour,myShoppingList);
        assertEquals(price, carrefourMangoPrice + carrefourMelonPrice);
    }
}