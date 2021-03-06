package ulb.infof307.g01.controller.shop;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.ProductDao;
import ulb.infof307.g01.model.database.dao.ShopDao;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ShopViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


/**
 * Contrôleur lié à au magasin
 */
public class ShopController extends Controller implements ShopViewController.Listener {

    public static final String SHOW_SHOP_FXML = "Shop.fxml";
    private ShopViewController viewController;
    private Shop shop;
    private final boolean isModifying; // POPUP pour la modification ou non
    private ShopListener listener;
    private final ShopDao shopDao;

    public ShopController(boolean isModifying){
        this.isModifying = isModifying;
        shopDao = configuration.getShopDao();
        shop = new Shop();
    }

    public void setListener(ShopListener listener){
        this.listener = listener;
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
                viewController.setBasicShopTextField(shop.getName(),shop.getAddress());
            }
            setStage(shopStage);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(SHOW_SHOP_FXML);
        }
    }

    /**
     * ajoutes les products du wrappeur dans le modèle produit
     * @param productsWrapper une collection de produit dans un Wrapper
     */
    private void addAllWrappedProduct(Collection<ShopViewController.ProductWrapper> productsWrapper){
        for(ShopViewController.ProductWrapper productWrapper: productsWrapper){
            shop.add(new Product.ProductBuilder().withName(productWrapper.getProductName()).withPrice(productWrapper.getProductPrice()).build());
        }
    }


    /**
     * Sauvegarde le magasin crée par l'utilisateur
     * @param shopName le nom du magasin
     * @param shopAddress l'adresse du magasin
     * @throws SQLException erreur au niveau de la base de donnée
     * @return Vrai si lFe magasin a été ajouté
     */
    @Override
    public boolean onSaveShopClicked(String shopName, String shopAddress) throws SQLException {
        boolean isSaved = true;
        try {
            addAllWrappedProduct(viewController.getTableViewShopItems());
            shop.setName(shopName);
            shop.setAddress(shopAddress);

            if (isModifying) {
                shopDao.update(shop);
                listener.update();
            }
            else {
                if(shopDao.get(shop.getName(), shop.getCoordinate()) == null){
                    shopDao.insert(shop);
                }
                else {
                    viewController.showErrors();
                    isSaved = false;
                }
            }
        }
        catch (NullPointerException e) {
            ViewController.showAlert(Alert.AlertType.ERROR, "L'adresse entrer n'existe pas", "");
            return false;
        }
        return isSaved;
    }

    /**
     * remplie la Table view avec les produits du magasin
     */
    @Override
    public void fillTableViewShop() {
        List<ShopViewController.ProductWrapper> productList = shop.stream().map(product -> new ShopViewController.ProductWrapper(product.getName(),product.getPrice())).toList();
        viewController.getTableViewShopItems().addAll(productList);
    }

    /**
     * Remplie la combo box avec les produits
     * @param productComboBox le combo box à remplir
     * @throws SQLException erreur au niveau de la base de donnée
     */
    @Override
    public void fillComboBoxProduct(ComboBox<String> productComboBox) throws SQLException {
        List<String> allProduct;
        ProductDao productDao = configuration.getProductDao();
        allProduct = productDao.getAllName();
        productComboBox.setItems(FXCollections.observableArrayList(allProduct));
    }

    /**
     * Ajout du produit a la table view
     * @param nameProduct le nom du produit
     * @param priceProduct le prix du produit
     */
    @Override
    public void onAddProductClicked(String nameProduct, double priceProduct){
        ShopViewController.ProductWrapper product = new ShopViewController.ProductWrapper(nameProduct, priceProduct);
        viewController.getTableViewShopItems().add(product);

    }

    /**
     * lance la fenêtre pour créer un nouveau produit
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
        int numberOfImageHelp = 8;
        HelpController helpController = new HelpController("helpShop/", numberOfImageHelp);
        helpController.displayHelpShop();
    }

    @Override
    public boolean deleteShop() {
        if(!isModifying)return false;
        boolean isDelete = false;
        ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer magasin ?", "Etes vous sur de vouloir supprimer ce magasin");
        if (alertResult == ButtonType.OK ) {
            try {
                shopDao.delete(shop);
                listener.update();
                isDelete = true;

            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }
        }
        return isDelete;
    }

    public interface ShopListener{
        void update();
    }

}
