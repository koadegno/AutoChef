package ulb.infof307.g01.view.shop;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * La classe gère la vue pour la création et la modification d'un magasin
 */
public class ShopViewController extends ViewController<ShopViewController.Listener> implements Initializable {

    public TableView<ProductWrapper> tableViewShop;
    public ComboBox<String> comboBoxProduct;
    public Spinner<Double> spinnerPrice;
    public TableColumn<ProductWrapper,String> columnProduct;
    public TableColumn<ProductWrapper,Double> columnPrice;
    public TextField nameShopTextField;
    public TextField addressShopTextField;
    public VBox vBox;


    public void createPopup(){
        try {
            listener.fillTableViewShop();
            listener.fillComboBoxProduct(comboBoxProduct);

        } catch (SQLException e) {
            showErrorSQL();
        }
    }

    public void setBasicShopTextField(String nameShop, String shopAddress){

        nameShopTextField.setText(nameShop);
        addressShopTextField.setText(shopAddress);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewShop.setEditable(false);
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
    }


    public void fillTableViewShop(){
        listener.fillTableViewShop();
    }


    private void fillComboboxProduct(){
        try {
            listener.fillComboBoxProduct(comboBoxProduct);
        } catch (SQLException e) {
            showErrorSQL();
        }
    }

    @FXML
    public void createNewProduct(){
        listener.createNewProductClicked();
    }

    public void addProductToTableView(){
        setNodeColor(comboBoxProduct, false);
        String nameProduct  = comboBoxProduct.getSelectionModel().getSelectedItem();
        if (nameProduct == null) setNodeColor(comboBoxProduct, true);
        else{
            double priceProduct = spinnerPrice.getValue();
            listener.onAddProductClicked(nameProduct,priceProduct);
        }
    }

    @FXML
    public void saveNewShop() {
        setNodeColor(nameShopTextField, false);
        String shopName = nameShopTextField.getText();
        String shopAddress = addressShopTextField.getText();
        if (shopName.isEmpty()){
            setNodeColor(nameShopTextField, true);
            return;
        }
        if (shopAddress.isEmpty()){
            setNodeColor(addressShopTextField, true);
            return;
        }

        try {
            boolean isSaved = listener.onSaveShopClicked(shopName,shopAddress);
            if(isSaved){
                closePopUp();
            }
        } catch (SQLException e) {
            showErrorSQL();
        }

    }
    public void showErrors(){
        setNodeColor(addressShopTextField, true);
        setNodeColor(nameShopTextField, true);
    }


    private void closePopUp() {
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onDeleteShop() {
        boolean isDelete = listener.deleteShop();
        if(isDelete){
            closePopUp();
        }

    }

    public ObservableList<ProductWrapper> getTableViewShopItems() {
        return tableViewShop.getItems();
    }


    public void setNameProduct(String nameProduct){
        comboBoxProduct.getItems().add(nameProduct);
        comboBoxProduct.setValue(nameProduct);
    }

    public void helpShop(){
        listener.displayHelpShop();
    }


    public interface Listener{
        boolean onSaveShopClicked(String shopName, String shopAddress) throws SQLException;

        void fillTableViewShop();

        void fillComboBoxProduct(ComboBox<String> productComboBox) throws SQLException;

        void onAddProductClicked(String nameProduct, double priceProduct);

        void createNewProductClicked();

        void displayHelpShop();

        boolean deleteShop();
    }

    public static class ProductWrapper {
        String productName;
        double productPrice;

        public ProductWrapper(String productName, double productPrice) {
            this.productName = productName;
            this.productPrice = productPrice;
        }

        public String getProductName() {
            return productName;
        }

        public double getProductPrice() {
            return productPrice;
        }
    }
}
