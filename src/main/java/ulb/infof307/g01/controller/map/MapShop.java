package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import ulb.infof307.g01.controller.shop.ShopController;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ShopDao;

import java.sql.SQLException;
import java.util.List;

public class MapShop implements ShopController.ShopListener{
    private Configuration configuration;
    private ShopDao shopDao;
    private ShoppingList shoppingList ;
    private MapShopListener listener;

    private Graphic currentGraphicCircleMapShop;
    private Graphic currentGraphicTextMapShop;


    public MapShop(MapShopListener mapShopListener) {
        configuration = Configuration.getCurrent();
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

    public void showNewShop(Point mapPoint) {
        displayShop(new Shop(mapPoint), false);
    }

    @Override
    public void addCircle(int color, String textCircle, Point coordinate, Boolean isShop){
        listener.addCircle(color, textCircle, coordinate, isShop);
    }


    /**
     * Met a jour le magasin afficher sur la carte
     * @param shop le magasin existant qu'il faut mettre a jour
     */
    @Override
    public void updateShop(Shop shop){

        TextSymbol textSymbol = (TextSymbol) currentGraphicTextMapShop.getSymbol();
        textSymbol.setText(shop.getName());
    }

    /**
     * Cherche le magasin correspondant à la position et lance le popup
     * @throws SQLException erreur au niveau de la base de donnée
     */
    public void updateShop() throws SQLException {

        Point mapPoint = (Point) currentGraphicCircleMapShop.getGeometry();
        String shopName = ((TextSymbol) currentGraphicTextMapShop.getSymbol()).getText();

        Shop shopToModify = shopDao.get(shopName,mapPoint);
        displayShop(shopToModify, true);
    }

    private void displayShop(Shop shopToModify, boolean isModifying) {
        ShopController showShopController = new ShopController(shopToModify, isModifying, this);
        showShopController.show();
    }

    public void setSelectedShop(Graphic currentGraphicCircleMapShop, Graphic currentGraphicTextMapShop) {
        this.currentGraphicTextMapShop = currentGraphicTextMapShop;
        this.currentGraphicCircleMapShop = currentGraphicCircleMapShop;
    }

    public interface MapShopListener{
        void addCircle(int color, String textCircle, Point coordinate, Boolean isShop);
    }
}
