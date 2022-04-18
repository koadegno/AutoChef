package ulb.infof307.g01.model.db;

import com.esri.arcgisruntime.geometry.Point;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.db.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestShopDao {

    static private final String fruit = "Fruit";
    static private final String gram = "g";
    static private final Product peach = new Product("peche", 1, gram, fruit);
    static private final Product strawberry = new Product( "fraise", 1, gram, fruit);
    static private final Shop aldiShop = new Shop("1 aldi",new Point(0,0));
    static private final Shop lidlShop = new Shop("aldi Namur",new Point(0,1));
    static private final Shop aldiRueNeuve = new Shop("1 aldi Rue neuve",new Point(0,2));
    static private final Shop carrefourAnvers = new Shop("Carrefour Anvers", new Point(50,30));

    static private final String  databaseName = "test.sqlite";


    @BeforeAll
    static public void initConfig() throws SQLException {
        Configuration.getCurrent().setDatabase(databaseName);
        Configuration.getCurrent().getProductUnityDao().insert(gram);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);
        Configuration.getCurrent().getProductDao().insert(peach);
        Configuration.getCurrent().getProductDao().insert(strawberry);
        Configuration.getCurrent().getShopDao().insert(aldiShop);
        Configuration.getCurrent().getShopDao().insert(aldiRueNeuve);
        Configuration.getCurrent().getShopDao().insert(carrefourAnvers);

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
        assertEquals(aldiRueNeuve.getName(),allShopName.get(1));
    }

    @Test
    void testDelete() throws SQLException{

        Configuration.getCurrent().getShopDao().delete(carrefourAnvers);
        List<Shop> shopList = Configuration.getCurrent().getShopDao().getShops();
        assertNotEquals(shopList.get(shopList.size()-1).getName(),carrefourAnvers.getName());
        assertNotEquals(shopList.get(shopList.size()-1).getCoordinate(), new Point(50,30));



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

        List<Shop> shopList = Configuration.getCurrent().getShopDao().getShops();
        assertEquals(shopList.get(0).getName(),aldiShop.getName());
        assertEquals(shopList.get(0).getCoordinate(),aldiShop.getCoordinate());
        assertEquals(shopList.get(1).getName(), aldiRueNeuve.getName());
        assertEquals(shopList.get(1).getCoordinate(), aldiRueNeuve.getCoordinate());

    }

    @Test
    void testGet() throws SQLException {
        Shop shopInserted = Configuration.getCurrent().getShopDao().get(aldiShop.getName(), aldiShop.getCoordinate());

        assertEquals(aldiShop.getName(),shopInserted.getName());
        assertEquals(aldiShop.getCoordinate(),shopInserted.getCoordinate());

    }

    @Test
    void testFillShopWithProduct() throws SQLException{
        Point coordinate = new Point(89,40);
        Shop carrefourBruxelles = new Shop(98,"Carrefour Bruxelles",coordinate);
        carrefourBruxelles.add(peach); carrefourBruxelles.add(strawberry);

        Configuration.getCurrent().getShopDao().insert(carrefourBruxelles);
        Shop shopToTest = Configuration.getCurrent().getShopDao().get(carrefourBruxelles.getName(),coordinate);

        assertEquals(carrefourBruxelles.size(),shopToTest.size());

    }

}