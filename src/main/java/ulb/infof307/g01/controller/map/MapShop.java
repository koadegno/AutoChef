package ulb.infof307.g01.controller.map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.TextSymbol;
import org.apache.jena.atlas.lib.Pair;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ShopDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapShop {
    private final ShopDao shopDao;

    public MapShop() {
        Configuration configuration = Configuration.getCurrent();
        this.shopDao = configuration.getShopDao();
    }

    public List<Pair<Shop,Integer>> shopWithProductList(ShoppingList shoppingList) throws SQLException {
        List<Shop> shopListWithProducts = shopDao.getShopWithProductList(shoppingList);
        List<Shop> shopWithMinPriceForProductList =  shopDao.getShopWithMinPriceForProductList(shoppingList);
        List<Pair<Shop,Integer>> pairShopColor = new ArrayList<>();
        for(Shop shop: shopListWithProducts){
            int color = MapConstants.COLOR_BLACK;
            if(shopWithMinPriceForProductList.contains(shop)) color = MapConstants.COLOR_RED;
            pairShopColor.add(new Pair<>(shop,color));
        }
        return pairShopColor;
    }


}
