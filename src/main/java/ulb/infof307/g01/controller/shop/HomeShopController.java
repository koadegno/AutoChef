package ulb.infof307.g01.controller.shop;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.view.shop.HomeShopViewController;

public class HomeShopController extends Controller implements HomeShopViewController.HomeShopListener {


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

    }

    @Override
    public void onBackButtonClick() {

    }

    @Override
    public void onAddShopClicked() {

    }

    @Override
    public void logout() {
        userLogout();
    }
}
