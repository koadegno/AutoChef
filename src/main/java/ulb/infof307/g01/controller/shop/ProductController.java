package ulb.infof307.g01.controller.shop;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.export.JSON;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.shop.ProductViewController;
import ulb.infof307.g01.view.shop.ShopViewController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ProductController extends Controller implements ProductViewController.Listener {
    private ProductViewController productViewController;
    private ShopViewController shopViewController;
    private Stage createProductStage;

    public ProductController(ShopViewController shopViewController){
        this.shopViewController = shopViewController;
    }

    public void displayCreateNewProduct(){
        productViewController = new ProductViewController();
        String nameCreateProductFXML = "CreateProduct.fxml";
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
                    userProduct = new Product(nameProduct, nameProductUnity ,nameProductFamily);
                    try {
                        Configuration.getCurrent().getProductDao().insert(userProduct);
                        shopViewController.setNameProduct(nameProduct);
                        createProductStage.close();
                    } catch (SQLException e) {
                        ProductViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Ce produit existe déjà. Veuillez préciser la marque s'il le faut");
                    }
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
        final String windowTitle = "Importer un PRODUIT depuis un fichier JSON";

        File jsonProduct = importJSON(windowTitle);

        if(jsonProduct != null){
            JSON json = new JSON();
            try {
                json.importProduct(jsonProduct.getAbsolutePath());
                String nameProduct = json.getNameProduct();
                shopViewController.setNameProduct(nameProduct);
            } catch (SQLException e) {
                String messageError = "Le contenu du JSON est incorrecte ou \nle produit existe deja";
                ProductViewController.showAlert(Alert.AlertType.ERROR, "Erreur", messageError);
               }
            createProductStage.close();
        }
        else{
            String messageError = "Vous n'avez pas importer un fichier JSON";
            ProductViewController.showAlert(Alert.AlertType.ERROR, "Erreur", messageError);
        }
    }

    @Override
    public void returnShopPage() {
        createProductStage.close();
    }
}
