package ulb.infof307.g01.controller.shop;

import javafx.fxml.FXMLLoader;
import org.jetbrains.annotations.NotNull;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.database.dao.ShopDao;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ShopsViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopsController extends Controller implements ShopsViewController.Listener, ListenerBackPreviousWindow, ShopController.ShopListener {

    private final ShopDao shopDao;
    private ShopsViewController shopsViewController;


    public ShopsController(ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        shopDao = configuration.getShopDao();
    }

    public void displayShops(){
        FXMLLoader loader = loadFXML("Shops.fxml");
        shopsViewController = loader.getController();
        shopsViewController.setListener(this);
        try {
            List<String> allShopName = getShopsStringsList();
            shopsViewController.displayShops(allShopName);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    @NotNull
    private List<String> getShopsStringsList() throws SQLException {
        List<Shop> allShop = shopDao.getAllShops();
        List<String> allShopName = new ArrayList<>();
        for(Shop shop: allShop){
            allShopName.add(shop.getName()+"-"+shop.getAddress());
        }
        return allShopName;
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
        if(shopName != null && !shopName.isEmpty() && !shopName.isBlank()){
            try {
                Shop shop = getShop(shopName);
                ShopController shopController = new ShopController(true);
                shopController.setShop(shop);
                shopController.setListener(this);
                shopController.displayShop();
            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }
        }

    }

    private Shop getShop(String shopNameAddress) throws SQLException {
        String[] shopNameSplit = shopNameAddress.split("-"); // 0 =  le nom , 1 = l'adresse
        String shopName = shopNameSplit[0];
        String shopAddress = shopNameSplit[1];
        Shop shop = new Shop.ShopBuilder().withName(shopName).withAddress(shopAddress).build();
        shop = shopDao.get(shop.getName(),shop.getCoordinate());
        return shop ;
    }

    @Override
    public void onReturn() {
        displayShops();
    }

    @Override
    public void update() {
        try {
            List<String> shopList = getShopsStringsList();
            shopsViewController.displayShops(shopList);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }

    }
}
