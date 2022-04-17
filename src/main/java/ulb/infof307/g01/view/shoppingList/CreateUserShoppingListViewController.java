package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Classe qui permet d'afficher la fenetre de creation d'une liste de courses
 */

public class CreateUserShoppingListViewController extends ShoppingListViewController implements Initializable {

    @FXML
    TextField nameMyCreateShoppingList;
    @FXML
    TableColumn<Product, String> columnProduct;
    @FXML
    TableColumn<Product, String> columnQuantityOrNumber;
    @FXML
    TableColumn<Product, String> columnUnity;
    @FXML
    TableColumn<Product, Void> columnDelete;
    @FXML
    Label labelNameShoppingList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeElementVisibility(true);
        this.spinnerQuantityOrNumber.setValueFactory(spinnerValueFactory);
        spinnerQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));

        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
        returnToMenu.setOnAction((event) ->{
            returnShoppingList();
        });

    }

    /**
     * Methode qui rend visible les boutons utilisée
     */
    private void activeElementVisibility(boolean isVisible) {
        btnAddNewProduct.setVisible(isVisible);
        nameMyCreateShoppingList.setVisible(isVisible);
        labelNameShoppingList.setVisible(isVisible);
        comboBoxListUnity.setVisible(isVisible);
        comboBoxListProduct.setVisible(isVisible);
        spinnerQuantityOrNumber.setVisible(isVisible);
        btnConfirm.setVisible(isVisible);
    }

    /**
     * Methode permettant de creer une liste de courses dans la base de donnee
     */
    @FXML
    public void confirmMyCreateShoppingList() {
        String shoppingListName = nameMyCreateShoppingList.getText();
        int sizeTableViewDisplayProductList = tableViewDisplayProductList.getItems().size();
        listener.confirmUserCreateShoppingList(shoppingListName, sizeTableViewDisplayProductList);
    }

    //TODO: changer ça parce que ce n'est pas MVC
    /**Methode permettant de remplir le tableau des elements d'une liste de courses
     * @param myExistentShoppingList : liste de shopping contenant la liste de courses
     */
    public void fillTableViewWithExistentShoppingList(ShoppingList myExistentShoppingList){
        tableViewDisplayProductList.getItems().clear();
        Vector<Product> temp = new Vector<>(myExistentShoppingList);
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        tableViewDisplayProductList.setItems(data);
        //Retour menu precedent : MainShoppingList
        returnToMenu.setOnAction((event) ->{
            returnToMyMenu();
        });
    }

    public void setReturnButtonAction(){
        returnToMenu.setOnAction((event) -> returnToMyMenu());
    }

    public void clearProductTableView(){
        tableViewDisplayProductList.getItems().clear();
    }

    public void setProductTableView(ObservableList<Product> data){
        tableViewDisplayProductList.setItems(data);
    }

    @Override
    public void removeBorderColor() {
        super.removeBorderColor();
        this.setNodeColor(nameMyCreateShoppingList, false);
    }

    public void showNameUserCreateShoppingListError(){
        this.setNodeColor(nameMyCreateShoppingList,true);
    }

    public void showIsEmptyTableViewError(){
        this.setNodeColor(tableViewDisplayProductList,true);
    }
}
