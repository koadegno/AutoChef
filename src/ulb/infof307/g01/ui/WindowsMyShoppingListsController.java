package ulb.infof307.g01.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ulb.infof307.g01.cuisine.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WindowsMyShoppingListsController extends MyShoppingListsControllerTools implements Initializable {
    private Vector<Product> myVectorProduct = new Vector<>();

    @FXML
    Button btnConfirm, btnAddNewProduct;

    @FXML
     public void seeMyShoppingListTableView(ActionEvent event) throws IOException {
         Object nameMyShoppingList =  comboBoxShoppingNameList.getSelectionModel().getSelectedItem();

         if(Objects.equals(nameMyShoppingList, null)){
             //TODO: afficher une erreur partie 2
             isVisibleElementToModifyMyShoppingList(false);
         }
         else{
             currentShoppingListname = (String) nameMyShoppingList;
             try { // afficher les produits de la liste de course dans la table
                 ShoppingList shoppingList = dataBase.getShoppingListFromName(currentShoppingListname);
                 Vector<Product> temp =  (Vector<Product>) shoppingList;
                 final ObservableList<Product> data = FXCollections.observableArrayList(temp);
                 tableViewDisplayProductList.setItems(data);
                 isVisibleElementToModifyMyShoppingList(true);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        try {
            ShoppingList shoppingListInDataBase = dataBase.getShoppingListFromName(currentShoppingListname);
            ShoppingList shoppingListToSend = new ShoppingList(shoppingListInDataBase.getName(), shoppingListInDataBase.getId());

            fillShoppingListToSend(shoppingListToSend);
            dataBase.saveModifyShoppingList(shoppingListToSend);
            returnShoppingList(event);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void isVisibleElementToModifyMyShoppingList(boolean isVisible){
        comboBoxListProduct.setVisible(isVisible);
        spinnerQuantityOrNumber.setVisible(isVisible);
        comboBoxListUnity.setVisible(isVisible);
        btnConfirm.setVisible(isVisible);
        btnAddNewProduct.setVisible(isVisible);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.spinnerQuantityOrNumber.setValueFactory(spinnerValueFactory);
        spinnerQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));


        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        CreateColWithButtonDelete createColWithButtonDelete = new CreateColWithButtonDelete();
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButtonDelete.createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
    }

    @Override
    public void initComboBox(){
        super.initComboBox();
        fillComboBoxShoppingNameList(comboBoxShoppingNameList, 1);
    }

}
