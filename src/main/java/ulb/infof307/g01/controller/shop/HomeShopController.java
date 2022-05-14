package ulb.infof307.g01.controller.shop;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.shop.HomeShopViewController;

public class HomeShopController extends Controller implements HomeShopViewController.HomeShopListener, ListenerBackPreviousWindow {


    public HomeShopController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
    }

    public void displayHomeShop(){
        FXMLLoader loader = loadFXML("HomeShop.fxml");
        HomeShopViewController viewController = loader.getController();
        viewController.setListener(this);
    }


    @Override
    public void onUpdateShopClicked() {
        ShopsController shopsController = new ShopsController(this);
        shopsController.displayShops();
    }

    /**
     * Revient à la page d'accueil de l'application.
     * @see HomePageViewController
     * */
    @Override
    public void onBackButtonClick() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void onAddShopClicked() {
        ShopController shopController = new ShopController(false);
        shopController.setShop(new Shop());
        shopController.displayShop();
    }

    @Override
    public void logout() {
        userLogout();
    }

    @Override
    public void onReturn() {
        displayHomeShop();
    }
}
