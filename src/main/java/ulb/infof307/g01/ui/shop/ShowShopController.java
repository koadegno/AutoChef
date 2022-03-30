package ulb.infof307.g01.ui.shop;

import com.esri.arcgisruntime.geometry.Point;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.ui.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ShowShopController extends Window implements Initializable {

    public TableView tableViewShop;
    public ComboBox comboboxProduct;
    public Spinner<Double> spinnerPrice;
    public Button buttonAdd;
    public TableColumn columnProduct;
    public TableColumn columnPrice;
    private Shop shop = null;


    public void createPopup(){
        try { popupFXML("ShowShop.fxml", this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initElement();
    }


    public void initElement(){
        //TODO: connection avec la bdd avec id
        fakeBDD();
        fillTableViewShop();
        fillComboboxProduct();
    }


    private void fakeBDD(){
        // TODO Supprimer
        shop = new Shop("Aldi", new Point(88,0));
        Product product1 = new Product("Pomme", 9.0);
        Product product2 = new Product("Banane",9.0);
        shop.add(product1);
        shop.add(product2);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewShop.setEditable(false);
        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
    }


    public void fillTableViewShop(){
        Set<Product> productWithPrice = (HashSet<Product>) shop;
        tableViewShop.getItems().addAll(productWithPrice);
    }


    private void fillComboboxProduct(){
        ArrayList<String> allProduct = null;
        try {allProduct = this.applicationConfiguration.getCurrent().getDatabase().getAllProductName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboboxProduct.setItems(FXCollections.observableArrayList(allProduct));
    }


    public void addProductToTableView(){
        String nameProduct  = comboboxProduct.getSelectionModel().getSelectedItem().toString();
        double priceProduct = spinnerPrice.getValue();
        Product product = new Product(nameProduct, priceProduct);
        shop.add(product);
        tableViewShop.getItems().addAll(product);
        //TODO: envoyer shop a la base de donnee
    }
}
