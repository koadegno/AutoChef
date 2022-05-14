package ulb.infof307.g01.controller.shop;

import javafx.fxml.FXMLLoader;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.shop.ShopViewController;
import ulb.infof307.g01.view.shop.ShopsViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopsController extends Controller implements ShopsViewController.ShopsListener, ListenerBackPreviousWindow {

    public ShopsController(ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);

    }

    public void displayShops(){
        FXMLLoader loader = loadFXML("Shops.fxml");
        ShopsViewController viewController = loader.getController();
        viewController.setListener(this);
        try {
            List<Shop> allShop = Configuration.getCurrent().getShopDao().getShops();
            List<String> allShopName = new ArrayList<>();
            for(Shop shop: allShop){
                allShopName.add(shop.getName()+"-"+shop.getAddress());
            }
            viewController.displayShops(allShopName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void logout() {
        userLogout();
    }

    /**
     * Revient à la page d'accueil de l'application.
     * @see HomePageViewController
     * */
    @Override
    public void onReturnButton() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void onShopTableViewClicked(String shopName) {
        System.out.println("le texte : "+ shopName);
        if(!shopName.isEmpty() && !shopName.isBlank()){

            Shop shop = getShop(shopName);
            ShopController shopController = new ShopController(true);
            shopController.setShop(shop);
            shopController.displayShop();
        }

    }

    private Shop getShop(String shopName){
        String[] shopNameSplit = shopName.split("-"); // 0 =  le nom , 1 = l'adresse
        return new Shop(shopNameSplit[0],shopNameSplit[1]);
    }

    @Override
    public void onReturn() {
        displayShops();
    }
}
