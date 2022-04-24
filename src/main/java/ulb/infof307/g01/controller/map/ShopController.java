package ulb.infof307.g01.controller.map;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ProductViewController;
import ulb.infof307.g01.view.shop.ShopViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopController extends Controller implements ShopViewController.Listener {

    public static final String SHOW_SHOP_FXML = "ShowShop.fxml";
    private ShopViewController viewController;
    private final ShopListener listener;
    private final Shop shop;
    private final boolean isModifying; // POPUP pour la modification ou non

    public ShopController(Shop shop, boolean isModifying, ShopListener listener){
        this.listener = listener;
        this.shop = shop;
        this.isModifying = isModifying;
    }

    /**
     * Lance l'affichage de la carte
     */
    public void show(){
        try {
            viewController = new ShopViewController();
            Stage shopStage = popupFXML(SHOW_SHOP_FXML,viewController);
            viewController.setListener(this);
            viewController.createPopup();
            if(isModifying) viewController.setNameShopTextField(shop.getName());
            setStage(shopStage);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(SHOW_SHOP_FXML);
        }
    }

    /**
     * Sauvegarde le magasin crée par l'utilisateur
     * @param shopName le nom du magasin
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
    public void onSaveShopClicked(String shopName) throws SQLException {
        shop.setName(shopName);
        shop.addAll(viewController.getTableViewShopItems());
        if(isModifying){
            Configuration.getCurrent().getShopDao().update(shop);
            listener.updateShop(shop);
        }
        else{
            Configuration.getCurrent().getShopDao().insert(shop);
            listener.addShop(shop);
        }
    }

    /**
     * remplie la Table view avec les produits du magasin
     */
    @Override
    public void fillTableViewShop() {
        viewController.getTableViewShopItems().addAll(shop);
    }

    /**
     * Remplie la combo box avec les produits
     * @param productComboBox le combo box à remplir
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
    public void fillComboBoxProduct(ComboBox<String> productComboBox) throws SQLException {
        ArrayList<String> allProduct;
        allProduct = Configuration.getCurrent().getProductDao().getAllName();
        productComboBox.setItems(FXCollections.observableArrayList(allProduct));
    }

    /**
     * Ajout du produit a la table view
     * @param nameProduct le nom du produit
     * @param priceProduct le prix du produit
     */
    @Override
    public void onAddProductClicked(String nameProduct, double priceProduct){
        Product product = new Product(nameProduct, priceProduct);
        viewController.getTableViewShopItems().add(product);

    }

    @Override
    public void createNewProductClicked() {
        ProductController productController = new ProductController();
        productController.displayCreateNewProduct();
    }

    /**
     * Permet l'ajout et la modification dans MapController
     */
    public interface ShopListener {
        void addShop(Shop shop);
        void updateShop(Shop shop);
    }
}
