package ulb.infof307.g01.view.shop;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g01.view.ViewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * La classe gère la vue pour l'affichage d'une recette favorite
 */

public class ShopsViewController extends ViewController<ShopsViewController.ShopsListener> implements Initializable {
    @FXML
    TableView<String> shopsTableView;
    @FXML
    TableColumn<String, String> shopsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shopsColumn.setCellValueFactory(feature -> {
            return new SimpleStringProperty(feature.getValue());
        });
    }

    public void displayShops(List<String> shops) {
        shopsTableView.getItems().addAll(shops);
    }
    public void onReturnButton() {
        listener.onReturnButton();
    }

    public void logout() {
        listener.logout();
    }

    public void OnShopTableView() {
        String shopName = shopsTableView.getSelectionModel().getSelectedItem();
        listener.onShopTableViewClicked(shopName);
    }

    public  interface ShopsListener{
        void logout();
        void onReturnButton();
        void onShopTableViewClicked(String shopName);
    }

}
