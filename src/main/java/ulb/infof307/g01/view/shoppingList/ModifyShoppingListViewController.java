package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * La classe gère la vue pour la modification d'un menu
 */

public class ModifyShoppingListViewController extends ShoppingListViewController {
    @FXML
    Button btnConfirm, btnAddNewProduct;

    /**
     * Permet d'afficher sur le tableView les listes de courses à partir d'un nom d'une
     * liste de courses
     */
    @FXML
     public void seeMyShoppingListTableView() {
         Object nameUserShoppingList =  comboBoxShoppingNameList.getSelectionModel().getSelectedItem();
         listener.seeUserShoppingList(nameUserShoppingList);
    }

    @FXML
    public void viewShoppingListOnMap() {
        Object nameUserShoppingList =  comboBoxShoppingNameList.getSelectionModel().getSelectedItem();
        listener.viewShoppingListOnMap(nameUserShoppingList);
    }

    public void setCurrentShoppingListName(String currentShoppingListName){
        this.currentShoppingListName = currentShoppingListName;
    }

    public void addProductListToTableView(Vector<Product> productOfShoppingList){
        tableViewDisplayProductList.setItems(FXCollections.observableArrayList(productOfShoppingList));
    }

    /**
     * Permet d'enregistrer une liste de courses que l'utilisateur aurait modifié
     */
    @FXML
    public void confirmMyCreateShoppingList() {
        listener.confirmUserModifyShoppingList(currentShoppingListName);
    }

    @FXML
    public void isVisibleElementToModifyMyShoppingList(boolean isVisible){
        comboBoxListProduct.setVisible(isVisible);
        spinnerQuantityOrNumber.setVisible(isVisible);
        comboBoxListUnity.setVisible(isVisible);
        btnConfirm.setVisible(isVisible);
        btnAddNewProduct.setVisible(isVisible);
        btnExportShoppingList.setVisible(isVisible);
        btnSendMail.setVisible(isVisible);
        btnSeeShoppingListOnMap.setVisible(isVisible);
    }

    protected void activeElementVisibility() {
        comboBoxShoppingNameList.setVisible(true);
        btnConfirm.setText("Enregistrer");
        btnSeeShoppingList.setVisible(true);
        btnSeeShoppingList.setOnAction(e-> seeMyShoppingListTableView());
        btnSeeShoppingListOnMap.setOnAction(e-> viewShoppingListOnMap());
    }

    public void initComboBox(List<String> allProduct, List<String> allUnitName, List<String> allShoppinListName ){
        super.initComboBox(allProduct, allUnitName);
        comboBoxShoppingNameList.setItems(FXCollections.observableArrayList(allShoppinListName));
    }


    public void initForCreateRecipe(ShoppingList shoppingList) { //TODO: reformer
        List<Product> productOfShoppingList = new ArrayList<>(shoppingList);
        tableViewDisplayProductList.setItems(FXCollections.observableArrayList(productOfShoppingList));
        isVisibleElementToModifyMyShoppingList(true);
        returnToMenu.setOnAction(event -> listener.cancelRecipeCreation());
        btnConfirm.setOnAction(event -> {
            fillShoppingListToSend();
            listener.returnAddedProducts();
        });

        comboBoxShoppingNameList.setVisible(false);
        btnSeeShoppingList.setVisible(false);
        btnSeeShoppingListOnMap.setVisible(false);
        btnExportShoppingList.setVisible(false);
        btnSendMail.setVisible(false);
        helpMenuShoppingList.setVisible(false);
    }

    public void helpShoppingList(){
        listener.helpModifyShoppingList();
    }


}
