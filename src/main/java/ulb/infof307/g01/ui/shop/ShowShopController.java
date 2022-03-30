package ulb.infof307.g01.ui.shop;

import com.esri.arcgisruntime.geometry.Point;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Shop;
import ulb.infof307.g01.ui.Window;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ShowShopController extends Window implements Initializable {


    public TableView tableViewShop;
    public ComboBox comboboxProduct;
    public Spinner spinnerPrice;
    public Button buttonAdd;
    public TableColumn columnProduct;
    public TableColumn columnPrice;

    public void createPopup() throws IOException {
       popupFXML("ShowShop.fxml", this);
       initElement();
    }

    public void initElement(){
        //TODO: connection avec la bdd avec id
        fillTableViewShop(fakeBDD());
    }

    private Shop fakeBDD(){
        // TODO Supprimer
        Shop shop = new Shop("Aldi", new Point(88,0));
        Product product1 = new Product("Pomme", 9, "kg",9 );
        Product product2 = new Product("Banane", 9, "kg",9 );
        shop.add(product1);
        shop.add(product2);
        return shop;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewShop.setEditable(false);
        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));

    }

    public void fillTableViewShop(Shop shop){
        Set<Product> productWithPrice =  (HashSet<Product>) shop;
        tableViewShop.getItems().addAll(productWithPrice);
    }

}
