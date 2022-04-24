package ulb.infof307.g01.view.shop;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ulb.infof307.g01.view.ViewController;

import java.util.ArrayList;

public class ProductViewController extends ViewController<ProductViewController.Listener> {

    public ComboBox<String> nameProductFamilyCombobox, nameProductUnityCombobox;
    public TextField nameProductTextField;

    public void initComboboxInformation(ArrayList<String> nameProductFamily, ArrayList<String> nameProductUnity){
        nameProductFamilyCombobox.setItems(FXCollections.observableArrayList(nameProductFamily));
        nameProductUnityCombobox.setItems(FXCollections.observableArrayList(nameProductUnity));
    }

    public void confirmCreateProduct(){
        String nameProduct = nameProductTextField.getText();
        String nameProductFamily  = nameProductFamilyCombobox.getSelectionModel().getSelectedItem();
        String nameProductUnity = nameProductUnityCombobox.getSelectionModel().getSelectedItem();
        listener.confirmCreateProduct(nameProduct, nameProductFamily, nameProductUnity);
    }

    public void importProductJSONFile(){
        listener.importProductJsonFile();
    }

    public void returnShopPage(){
        listener.returnShopPage();
    }

    public void showErrorNotChooseNameProductUnity(boolean isError){
        this.setNodeColor(nameProductUnityCombobox, isError);
    }

    public void showErrorNotChooseNameProductFamily(boolean isError){
        this.setNodeColor(nameProductFamilyCombobox, isError);
    }

    public void showErrorNameProduct(boolean isError){
        this.setNodeColor(nameProductTextField, isError);
    }

    public void removeShowErrorProduct(boolean isError){
        showErrorNameProduct(isError);
        showErrorNotChooseNameProductUnity(isError);
        showErrorNotChooseNameProductFamily(isError);
    }


    public interface Listener{
        void confirmCreateProduct(String nameProduct, String nameProductFamily, String nameProductUnity);
        void importProductJsonFile();
        void returnShopPage();
    }
}
