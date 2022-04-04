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
    static private Shop aldiShop = new Shop(1,"1aldi",new Point(0,0));
    static private Shop lidlShop = new Shop(2,"aldi Namur",new Point(0,1));
    static private Shop aldi2Shop = new Shop(3,"aldi Vilvorde",new Point(0,2));
    static private Shop aldi4Shop = new Shop(4,"1aldi Rue neuve",new Point(0,2));

    static private String  databaseName = "test.sqlite";


    @BeforeAll
    static public void initConfig() throws SQLException {
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductUnityDao().insert(gram);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);
        Configuration.getCurrent().getProductDao().insert(peach);
        Configuration.getCurrent().getShopDao().insert(aldiShop);
        Configuration.getCurrent().getShopDao().insert(aldi4Shop);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of(databaseName));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> allShopName = Configuration.getCurrent().getShopDao().getAllName();
        assertEquals(aldiShop.getName(),allShopName.get(0));
        assertEquals(aldi4Shop.getName(),allShopName.get(1));
    }

    @Test
    void testInsert() throws SQLException {
        Point point = new Point(lidlShop.getCoordinateX(), lidlShop.getCoordinateY());
        Configuration.getCurrent().getShopDao().insert(lidlShop);
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(lidlShop.getName(), point);

        assertNotNull(shopInserted);
        assertEquals(lidlShop.getName(),shopInserted.getName());
        assertEquals(lidlShop.getCoordinate(),shopInserted.getCoordinate());

    }

    @Test
    void testUpdate() throws SQLException {
        Point coordinate = new Point(1,1);
        Shop aldiBruxellesShop = new Shop(2,"aldi Bruxelles",coordinate);
        Configuration.getCurrent().getShopDao().insert(aldiBruxellesShop);
        aldiBruxellesShop = new Shop(2,"aldi Zaventem",coordinate);
        Configuration.getCurrent().getShopDao().update(aldiBruxellesShop);
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(aldiBruxellesShop.getName(),aldiBruxellesShop.getCoordinate());

        assertEquals(aldiBruxellesShop.getName(),shopInserted.getName());
        assertEquals(aldiBruxellesShop.getCoordinate(),shopInserted.getCoordinate());

    }

    @Test
    void testGetShops() throws SQLException {

        //TODO MODIFIER


    }

    @Test
    void testGet() throws SQLException {
        Point point = new Point(aldiShop.getCoordinateX(), aldiShop.getCoordinateY());
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(aldiShop.getName(), point);

        assertEquals(aldiShop.getName(),shopInserted.getName());
        assertEquals(aldiShop.getCoordinate(),shopInserted.getCoordinate());

    }

}