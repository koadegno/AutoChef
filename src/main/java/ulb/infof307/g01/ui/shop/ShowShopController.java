package ulb.infof307.g01.ui.shop;

import com.esri.arcgisruntime.geometry.Point;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.ui.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ShowShopController extends Window implements Initializable {

    public TableView tableViewShop;
    public ComboBox comboboxProduct;
    public Spinner<Double> spinnerPrice;
    public TableColumn columnProduct;
    public TableColumn columnPrice;
    public TextField nameShop;
    public VBox vBox; 
    private Shop shop = null;


    public void createPopup(int idShop){
        try { popupFXML("ShowShop.fxml", this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO : demander a la base de donnée le Shop avec l'id
        initElement(idShop);
    }


    public void initElement(int idShop){
        //TODO: connection avec la bdd avec id
        if (idShop != 0){  //TODO: seulement un test
            fakeBDD();
            nameShop.setText(shop.getName());
            fillTableViewShop();
        }
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
        try {allProduct = Configuration.getCurrent().getProductDao().getAllName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboboxProduct.setItems(FXCollections.observableArrayList(allProduct));
    }


    public void addProductToTableView(){

        setNodeColor(comboboxProduct, false);
        Object nameProduct  = comboboxProduct.getSelectionModel().getSelectedItem();
        if (nameProduct == null) setNodeColor(comboboxProduct, true);
        else{
            double priceProduct = spinnerPrice.getValue();
            Product product = new Product(nameProduct.toString(), priceProduct);
            shop.add(product);
            tableViewShop.getItems().addAll(product);
        }

    }

    public void saveNewShop(){
        setNodeColor(nameShop, false);
        String getNameShop = nameShop.getText();

        if (!Objects.equals(getNameShop, "")){
            //TODO: envoyer shop a la base de donnee
            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.close();
        }
        else{
            setNodeColor(nameShop, true);
        }
    }
}