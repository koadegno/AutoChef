package ulb.infof307.g01.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.shop.WindowShopController;

import java.sql.SQLException;
import java.util.ArrayList;

public class ShopController extends Controller implements WindowShopController.Listener {

    private WindowShopController viewController;
    private MapListener listener;
    private Shop shop;
    private boolean isModifying; // POPUP pour la modification ou non

    public ShopController(Stage primaryStage, Shop shop, boolean isModifying, MapListener listener){
        this.listener = listener;
        this.setStage(primaryStage);
        this.shop = shop;
        this.isModifying = isModifying;
    }

    public void show(){
        viewController = new WindowShopController();
        viewController.setListener(this);
        viewController.createPopup();
        if(isModifying) viewController.setNameShopTextField(shop.getName());

        FXMLLoader loader = this.loadFXML("ShowShop.fxml");
        loader.setController(viewController);
        this.setNewScene(loader,"Magasin");
    }

    @Override
    public void onSaveShopClicked(String shopName) throws SQLException {
        shop.setName(shopName);
        if(isModifying){
            shop.addAll(viewController.getTableViewShopItems());
            Configuration.getCurrent().getShopDao().update(shop);
            listener.updateShop(shop);
        }
        else{
            shop.addAll(viewController.getTableViewShopItems());
            Configuration.getCurrent().getShopDao().insert(shop);
            listener.addShop(shop);
        }
    }

    @Override
    public void fillTableViewShop() {
        viewController.getTableViewShopItems().addAll(shop);
    }

    @Override
    public void fillComboBoxProduct(ComboBox<String> productComboBox) throws SQLException {
        ArrayList<String> allProduct;
        allProduct = Configuration.getCurrent().getProductDao().getAllName();
        productComboBox.setItems(FXCollections.observableArrayList(allProduct));
    }

    @Override
    public void onAddProductClicked(String nameProduct, double priceProduct){
        //TODO éviter les doublons ?
        Product product = new Product(nameProduct, priceProduct);
        viewController.getTableViewShopItems().add(product);

    }

    public interface MapListener {
        void addShop(Shop shop);
        void updateShop(Shop shop);
    }
}
