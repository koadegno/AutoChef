package ulb.infof307.g01.view.shop;


import javafx.fxml.FXML;
import ulb.infof307.g01.view.ViewController;

/**
 * Contrôleur de vue pour la page principale des magasins.
 * Elle permet à d'accéder à la page de création d'un magasin
 * et modifier ces magasins
 * */
public class HomeShopViewController extends ViewController<HomeShopViewController.HomeShopListener> {

    @FXML
    void logout() {
        listener.logout();
    }

    @FXML
    void onAddShopClicked() {
        listener.onAddShopClicked();
    }

    @FXML
    void onBackButtonClick() {
        listener.onBackButtonClick();
    }

    @FXML
    void onUpdateShopClicked() {
        listener.onUpdateShopClicked();
    }

    public interface HomeShopListener{
        void onUpdateShopClicked();
        void onBackButtonClick();
        void onAddShopClicked();
        void logout();
    }

}

