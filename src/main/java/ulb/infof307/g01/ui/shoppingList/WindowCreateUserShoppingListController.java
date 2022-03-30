package ulb.infof307.g01.ui.shoppingList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.ui.tools.CreateColWithButtonDelete;
import ulb.infof307.g01.ui.tools.WindowUserShoppingListsControllerTools;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe qui permet d'afficher la fenetre de creation d'une liste de courses
 */

public class WindowCreateUserShoppingListController extends WindowUserShoppingListsControllerTools implements Initializable {

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
                returnShoppingList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Methode qui rend visible les boutons utilisée
     */
    private void activeElementVisibility() {
        btnAddNewProduct.setVisible(true);
        nameMyCreateShoppingList.setVisible(true);
        labelNameShoppingList.setVisible(true);
        comboBoxListUnity.setVisible(true);
        comboBoxListProduct.setVisible(true);
        spinnerQuantityOrNumber.setVisible(true);
        btnConfirm.setVisible(true);
    }

    /**
     * Methode permettant de creer une liste de courses dans la base de donnee
     */
    @FXML
    public void confirmMyCreateShoppingList() {
        removeBorderColor();
        String shoppingListName = nameMyCreateShoppingList.getText();

        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            this.setNodeColor(nameMyCreateShoppingList,true);
        }
        else if(tableViewDisplayProductList.getItems().size() == 0){ // table view est vide
            this.setNodeColor(tableViewDisplayProductList,true);
        }
        else {
            ShoppingList shoppingListToSend = new ShoppingList(shoppingListName);
            fillShoppingListToSend(shoppingListToSend);
            try {
                this.applicationConfiguration.getCurrent().getDatabase().saveNewShoppingList(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                this.setNodeColor(nameMyCreateShoppingList,true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // else tout ce passe bien
            returnToMenu.fire();
        }
    }

    /**Methode permettant de remplir le tableau des elements d'une liste de courses
     * @param myExistentShoppingList : liste de shopping contenant la liste de courses
     */
    public void fillTableViewWithExistentShoppingList(ShoppingList myExistentShoppingList){
        tableViewDisplayProductList.getItems().clear();
        Vector<Product> temp =  (Vector<Product>) myExistentShoppingList;
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        tableViewDisplayProductList.setItems(data);
        //Retour menu precedent : MainShoppingList
        returnToMenu.setOnAction((event) ->{
            returnToMyMenu();
        });
    }
    @Override
    protected void removeBorderColor() {
        super.removeBorderColor();
        this.setNodeColor(nameMyCreateShoppingList, false);
    }
}
