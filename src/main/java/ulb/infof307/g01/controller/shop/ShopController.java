package ulb.infof307.g01.controller.shop;

import com.esri.arcgisruntime.geometry.Point;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.controller.map.MapConstants;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ShopViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Contrôleur lié à au magasin
 */
public class ShopController extends Controller implements ShopViewController.Listener {

    public static final String SHOW_SHOP_FXML = "Shop.fxml";
    private ShopViewController viewController;
    private Shop shop;
    private final boolean isModifying; // POPUP pour la modification ou non

    public ShopController(boolean isModifying){
        this.isModifying = isModifying;
        shop = null;
    }

    public ShopController(Shop shop, boolean isModifying){
        this.shop = shop;
        this.isModifying = isModifying;
    }

    public void setShop(Shop shop){
        this.shop = shop;
    }

    /**
     * Lance l'affichage de la carte
     */
    public void displayShop(){
        try {
            viewController = new ShopViewController();
            Stage shopStage = popupFXML(SHOW_SHOP_FXML,viewController);
            viewController.setListener(this);
            viewController.createPopup();
            if(isModifying){
                viewController.setBasicShopTextField(shop.getName(),shop.getAddress() );
            }
            else{
                shop = new Shop("","");
            }

            setStage(shopStage);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(SHOW_SHOP_FXML);
        }
    }

    /**
     * Sauvegarde le magasin crée par l'utilisateur
     * @param shopName le nom du magasin
     * @param shopAddress
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
    public void onSaveShopClicked(String shopName, String shopAddress) throws SQLException {
        shop.setName(shopName);
        shop.setAddress(shopAddress);
        shop.addAll(viewController.getTableViewShopItems());
        if (isModifying) {
            Configuration.getCurrent().getShopDao().update(shop);
        }
        else {
            Configuration.getCurrent().getShopDao().insert(shop);
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

    /**
     * lance la fenetre pour crée un nouveau produit
     */
    @Override
    public void createNewProductClicked() {
        ProductController productController = new ProductController(viewController);
        productController.displayCreateNewProduct();
    }

    /**
     * Affiche l'aide
     */
    @Override
    public void displayHelpShop() {
        int numberOfImageHelp = 14;
        HelpController helpController = new HelpController("helpShop/", numberOfImageHelp);
        helpController.displayHelpShop();
    }

}
