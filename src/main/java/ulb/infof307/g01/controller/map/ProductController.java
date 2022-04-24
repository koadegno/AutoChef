package ulb.infof307.g01.controller.map;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ProductViewController;
import ulb.infof307.g01.view.shop.ShopViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductController extends Controller implements ProductViewController.Listenner {
    private final ProductViewController productViewController;

    public ProductController(){
        productViewController = new ProductViewController();
    }

    public void displayCreateNewProduct(){
        String nameCreateProductFXML = "createProduct.fxml";
        try {
            Stage createProductStage = popupFXML(nameCreateProductFXML, productViewController);
            productViewController.setListener(this);
            this.initCreateProductFXML();
            //setStage(createProductStage);
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
    public void confirmCreateProduct() {
        //TODO: appeler la base de donnée pour lui envoyer le produit
    }

    @Override
    public void ImportProductJsonFile() {

    }

    @Override
    public void ReturnShopPage() {

    }
}
