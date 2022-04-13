package ulb.infof307.g01.view.shop;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.view.ViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WindowShopController extends ViewController<WindowShopController.Listener> implements Initializable {

    public TableView<Product> tableViewShop;
    public ComboBox<String> comboBoxProduct;
    public Spinner<Double> spinnerPrice;
    public TableColumn columnProduct;
    public TableColumn columnPrice;
    public TextField nameShopTextField;
    public VBox vBox; 


    public void createPopup(){
        try {
            listener.fillTableViewShop();
            listener.fillComboBoxProduct(comboBoxProduct);

        } catch (SQLException e) {
            showErrorSQL();
        }
    }

    public void setNameShopTextField(String nameShop){
        nameShopTextField.setText(nameShop);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewShop.setEditable(false);
        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
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

    public void addProductToTableView(){
        setNodeColor(comboBoxProduct, false);
        String nameProduct  = comboBoxProduct.getSelectionModel().getSelectedItem();
        if (nameProduct == null) setNodeColor(comboBoxProduct, true);
        else{
            double priceProduct = spinnerPrice.getValue();
            listener.onAddProductClicked(nameProduct,priceProduct);
        }
    }


    public void saveNewShop() {
        setNodeColor(nameShopTextField, false);
        String getNameShop = nameShopTextField.getText();

        if (!Objects.equals(getNameShop, "")){
            try {
                listener.onSaveShopClicked(getNameShop);
                Stage stage = (Stage) vBox.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                showErrorSQL();
            }
        }
        else{
            setNodeColor(nameShopTextField, true);
        }
    }

    public ObservableList<Product> getTableViewShopItems() {
        return tableViewShop.getItems();
    }


    public interface Listener{
        void onSaveShopClicked(String shopName) throws SQLException;

        void fillTableViewShop();

        void fillComboBoxProduct(ComboBox<String> productComboBox) throws SQLException;

        void onAddProductClicked(String nameProduct, double priceProduct);
    }
}
