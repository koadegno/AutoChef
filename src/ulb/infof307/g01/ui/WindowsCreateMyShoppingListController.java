package ulb.infof307.g01.ui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.ShoppingList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import static java.lang.System.exit;

public class WindowsCreateMyShoppingListController extends MyShoppingListsControllerTools implements Initializable {

    private Vector<Product> myListProduct = new Vector<>();

    @FXML
    TextField nameMyCreateShoppingList;
    @FXML
    TableColumn columnProduct, columnQuantityOrNumber, columnUnity, columnDelete;
    @FXML
    Label labelNameShoppingList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeElementVisibility();
        this.spinnerQuantityOrNumber.setValueFactory(spinnerValueFactory);
        spinnerQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));

        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        CreateColWithButtonDelete createColWithButtonDelete = new CreateColWithButtonDelete();
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButtonDelete.createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
        returnToMenu.setOnAction((event) ->{
            try {
                returnShoppingList(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void activeElementVisibility() {
        btnAddNewProduct.setVisible(true);
        nameMyCreateShoppingList.setVisible(true);
        labelNameShoppingList.setVisible(true);
        comboBoxListUnity.setVisible(true);
        comboBoxListProduct.setVisible(true);
        spinnerQuantityOrNumber.setVisible(true);
        btnConfirm.setVisible(true);
    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        removeBorderColor();
        String shoppingListName = nameMyCreateShoppingList.getText();

        System.out.println(shoppingListName);
        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            nameMyCreateShoppingList.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
        else if(tableViewDisplayProductList.getItems().size() == 0){ // table view est vide
            tableViewDisplayProductList.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
        else {
            ShoppingList shoppingListToSend = new ShoppingList(shoppingListName);
            fillShoppingListToSend(shoppingListToSend);
            try {
                dataBase.saveNewShoppingList(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                nameMyCreateShoppingList.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
                System.out.println(e.getMessage());
                return;

            } catch (SQLException e) {
                System.out.println("ERREUR HERE");
                e.printStackTrace();
                return;
            }
            // else tout ce passe bien
            returnShoppingList(event);
        }
    }

    public void fillTableViewWithExistentShoppingList(ShoppingList myExistentShppingList){
        tableViewDisplayProductList.getItems().clear();
        Vector<Product> temp =  (Vector<Product>) myExistentShppingList;
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        tableViewDisplayProductList.setItems(data);
        returnToMenu.setOnAction((event) ->{
            try {
                returnToMyMenu(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    protected void removeBorderColor() {
        super.removeBorderColor();
        nameMyCreateShoppingList.setStyle("");
    }

}
