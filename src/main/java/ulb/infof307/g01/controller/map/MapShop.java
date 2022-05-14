package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.TextSymbol;
import ulb.infof307.g01.controller.shop.ShopController;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ShopDao;

import java.sql.SQLException;
import java.util.List;

public class MapShop {
    private final ShopDao shopDao;
    private ShoppingList shoppingList ;
    private final MapShopListener listener;

    private Graphic currentGraphicCircleMapShop;
    private Graphic currentGraphicTextMapShop;


    public MapShop(MapShopListener mapShopListener) {
        Configuration configuration = Configuration.getCurrent();
        shopDao = configuration.getShopDao();
        listener = mapShopListener;
    }

    public void initAllShops() throws SQLException {
        List<Shop> allShopList  = shopDao.getShops();
        for(Shop shop: allShopList){
            listener.addCircle(MapConstants.COLOR_RED, shop.getName(), shop.getCoordinate(), true);
        }
    }

    public void displayShopsWithProductList() throws SQLException {
        List<Shop> shopListWithProducts = shopDao.getShopWithProductList(shoppingList);
        List<Shop> shopWithMinPriceForProductList =  shopDao.getShopWithMinPriceForProductList(shoppingList);
        for(Shop shop: shopListWithProducts){
            String toDisplay = shop.getName() + ": " + shopDao.getShoppingListPriceInShop(shop, shoppingList) + " €";
            int color = MapConstants.COLOR_BLACK;
            if(shopWithMinPriceForProductList.contains(shop)) color = MapConstants.COLOR_RED;
            listener.addCircle(color, toDisplay, shop.getCoordinate(), true);
        }
    }



    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }


    public void setSelectedShop(Graphic currentGraphicCircleMapShop, Graphic currentGraphicTextMapShop) {
        this.currentGraphicTextMapShop = currentGraphicTextMapShop;
        this.currentGraphicCircleMapShop = currentGraphicCircleMapShop;
    }

    public void deleteShop() throws SQLException {

        String shopName = ((TextSymbol) currentGraphicTextMapShop.getSymbol()).getText();
        Point shopPoint = (Point) currentGraphicTextMapShop.getGeometry();

        Shop shopToDelete = shopDao.get(shopName, shopPoint);
        shopDao.delete(shopToDelete);
    }

    public interface MapShopListener{
        void addCircle(int color, String textCircle, Point coordinate, Boolean isShop);
    }
}
