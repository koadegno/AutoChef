package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.util.Vector;

/**
 * Classe qui permet d'afficher la fenêtre de creation d'une liste de courses
 */

public class CreateShoppingListViewController extends ShoppingListViewController {

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

    /**
     * Methode qui rend visible les boutons utilisée
     */
    protected void updateShoppingList() {
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
        String shoppingListName = nameMyCreateShoppingList.getText();
        int sizeTableViewDisplayProductList = tableViewDisplayProductList.getItems().size();
        listener.confirmUserCreateShoppingList(shoppingListName, sizeTableViewDisplayProductList);
    }

    /**Methode permettant de remplir le tableau des elements d'une liste de courses
     * @param myExistentShoppingList : liste de shopping contenant la liste de courses
     */
    public void fillTableViewWithExistentShoppingList(ShoppingList myExistentShoppingList){
        tableViewDisplayProductList.getItems().clear();
        Vector<Product> temp = new Vector<>(myExistentShoppingList);
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        tableViewDisplayProductList.setItems(data);
        //Retour menu precedent : MainShoppingList
        returnToMenu.setOnAction((event) -> returnToMyMenu());
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

    public void helpShoppingList(){
        listener.helpCreateShoppingList();
    }
}
