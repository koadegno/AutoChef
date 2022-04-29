package ulb.infof307.g01.model.database;

import com.esri.arcgisruntime.geometry.Point;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestShopDao {

    static private final String FRUIT = "Fruit";
    static private final String GRAM = "g";
    static private final Product PEACH = new Product("peche", 1, GRAM, FRUIT);
    static private final Product STRAWBERRY = new Product( "fraise", 1, GRAM, FRUIT);
    static private final Shop ALDI_SHOP = new Shop("1 aldi",new Point(0,0));
    static private final Shop LIDL_SHOP = new Shop("aldi Namur",new Point(0,1));
    static private final Shop ALDI_RUE_NEUVE = new Shop("1 aldi Rue neuve",new Point(0,2));
    static private final Shop CARREFOUR_ANVERS = new Shop(3,"Carrefour Anvers", new Point(50,30));

    static private final String DATABASE_NAME = "test.sqlite";


    @BeforeAll
    static public void initConfig() throws SQLException {
        Configuration.getCurrent().setDatabase(DATABASE_NAME);

        User testUser = new User("admin","admin",true);
        testUser.setID(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getProductUnityDao().insert(GRAM);
        Configuration.getCurrent().getProductFamilyDao().insert(FRUIT);
        Configuration.getCurrent().getProductDao().insert(PEACH);
        Configuration.getCurrent().getProductDao().insert(STRAWBERRY);
        Configuration.getCurrent().getShopDao().insert(ALDI_SHOP);
        Configuration.getCurrent().getShopDao().insert(ALDI_RUE_NEUVE);
        Configuration.getCurrent().getShopDao().insert(CARREFOUR_ANVERS);

    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
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

}