package ulb.infof307.g01.controller.map;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ProductViewController;
import ulb.infof307.g01.view.shop.ShopViewController;

import java.io.IOException;

public class ProductController extends Controller implements ProductViewController.Listenner {
    private ProductViewController productViewController;

    public void displayCreateNewProduct(){
        String nameCreateProductFXML = "createProduct.fxml";
        try {
            Stage shopStage = popupFXML(nameCreateProductFXML, productViewController);
            productViewController.setListener(this);
            setStage(shopStage);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(nameCreateProductFXML);
        }
    }
}
