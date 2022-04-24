package ulb.infof307.g01.controller.map;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ProductViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ProductController extends Controller implements ProductViewController.Listener {
    private final ProductViewController productViewController;
    private Stage createProductStage;

    public ProductController(){
        productViewController = new ProductViewController();
    }

    public void displayCreateNewProduct(){
        String nameCreateProductFXML = "createProduct.fxml";
        try {
            createProductStage = popupFXML(nameCreateProductFXML, productViewController);
            productViewController.setListener(this);
            this.initCreateProductFXML();
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(nameCreateProductFXML);
        }
    }

    public void initCreateProductFXML(){
        ArrayList<String> nameProductFamily = null;
        ArrayList<String> nameProductUnity = null;
        try {
            nameProductFamily = Configuration.getCurrent().getProductFamilyDao().getAllName();
            nameProductUnity = Configuration.getCurrent().getProductUnityDao().getAllName();

        } catch (SQLException e) {
            //TODO: régler cette erreur
            e.printStackTrace();
        }
        productViewController.initComboboxInformation(nameProductFamily,nameProductUnity);
    }

    @Override
    public void confirmCreateProduct(String nameProduct, String nameProductFamily, String nameProductUnity) {
        productViewController.removeShowErrorProduct(false);

        Product userProduct;
        boolean isNameProduct = !Objects.equals(nameProduct, "");
        boolean isNameProductFamily = !Objects.equals(nameProductFamily, null);
        boolean isNameProductUnity = !Objects.equals(nameProductUnity, null);

        if(isNameProduct){
            if(isNameProductFamily){
                if(isNameProductUnity){
                    userProduct = new Product(nameProduct, nameProductFamily, nameProductUnity);
                    //TODO: envoyer ça a la bdd
                    //TODO: afficher dans le menu d'avant
                    createProductStage.close();
                }
                else{
                    productViewController.showErrorNotChooseNameProductUnity(true);
                }
            }
            else{
                productViewController.showErrorNotChooseNameProductFamily(true);
            }
        }
        else{
            productViewController.showErrorNameProduct(true);
        }

    }

    @Override
    public void importProductJsonFile() {

    }

    @Override
    public void returnShopPage() {
        createProductStage.close();
    }
}
