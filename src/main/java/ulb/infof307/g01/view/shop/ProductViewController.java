package ulb.infof307.g01.view.shop;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ulb.infof307.g01.view.ViewController;

import java.util.ArrayList;

public class ProductViewController extends ViewController {

    public ComboBox<String> nameProductFamilyCombobox, nameProductUnityCombobox;
    public TextField nameProduct;

    public void initComboboxInformation(ArrayList<String> nameProductFamily, ArrayList<String> nameProductUnity){
        nameProductFamilyCombobox.setItems(FXCollections.observableArrayList(nameProductFamily));
        nameProductUnityCombobox.setItems(FXCollections.observableArrayList(nameProductUnity));
    }

    public void confirmCreateProduct(){

    }

    public void ImportProductJSONFile(){

    }

    public void ReturnShopPage(){

    }

    public interface Listenner{
        void confirmCreateProduct();
        void ImportProductJsonFile();
        void ReturnShopPage();
    }
}
