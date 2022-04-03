package ulb.infof307.g01.db;

import com.esri.arcgisruntime.geometry.Point;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestShopDao {

    static String fruit = "Fruit";
    static String gram = "g";
    static private Product peach = new Product("peche", 1, gram, fruit);
    static private Product strawberry = new Product( "fraise", 1, gram, fruit);
    static private Shop aldiShop = new Shop(1,"aldi",new Point(0,0));
    static private Shop lidlShop = new Shop(2,"aldi Namur",new Point(0,1));
    static private Shop aldi2Shop = new Shop(3,"aldi Vilvorde",new Point(0,2));

    static private String  databaseName = "test.sqlite";


    @BeforeAll
    static public void initConfig() throws SQLException {
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductUnityDao().insert(gram);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);
        Configuration.getCurrent().getProductDao().insert(peach);
        Configuration.getCurrent().getShopDao().insert(aldiShop);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void testGetAllName() {
    }

    @Test
    void testInsert() throws SQLException {
        Point point = new Point(lidlShop.getCoordinateX(), lidlShop.getCoordinateY());
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(lidlShop.getName(), point);

        assertEquals(lidlShop.getID(),shopInserted.getID());
        assertEquals(lidlShop.getName(),shopInserted.getName());
        assertEquals(lidlShop.getCoordinate(),shopInserted.getCoordinate());

    }

    @Test
    void testUpdate() {
    }

    @Test
    void testGetShops() throws SQLException {
        List<Shop> allShopInserted =  Configuration.getCurrent().getShopDao().getShops("aldi");
        for(Shop shop:allShopInserted){
            Configuration.getCurrent().getShopDao().delete(shop);
        }

        Configuration.getCurrent().getShopDao().insert(aldiShop);
        Configuration.getCurrent().getShopDao().insert(lidlShop);
        Configuration.getCurrent().getShopDao().insert(aldi2Shop);

        List<Shop> shopList = Configuration.getCurrent().getShopDao().getShops("aldi");
        System.out.println(shopList);
        assertEquals(3,shopList.size());

    }

    @Test
    void testGet() throws SQLException {
        Point point = new Point(aldiShop.getCoordinateX(), aldiShop.getCoordinateY());
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(aldiShop.getName(), point);

        assertEquals(aldiShop.getID(),shopInserted.getID());
        assertEquals(aldiShop.getName(),shopInserted.getName());
        assertEquals(aldiShop.getCoordinate(),shopInserted.getCoordinate());

    }

}