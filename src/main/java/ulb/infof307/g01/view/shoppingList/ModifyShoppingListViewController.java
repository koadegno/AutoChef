package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.util.List;
import java.util.Vector;

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
    }

    protected void activeElementVisibility() {
        comboBoxShoppingNameList.setVisible(true);
        btnConfirm.setText("Enregistrer");
        btnSeeShoppingList.setVisible(true);
        btnSeeShoppingList.setOnAction(e-> seeMyShoppingListTableView());
    }

    public void initComboBox(List<String> allProduct, List<String> allUnitName, List<String> allShoppinListName ){
        super.initComboBox(allProduct, allUnitName);
        comboBoxShoppingNameList.setItems(FXCollections.observableArrayList(allShoppinListName));
    }


    public void initForCreateRecipe(ShoppingList shoppingList) { //TODO: reformer
        //TODO: regler ce probleme pour init
        //super.initComboBox(allProduct, allUnitName);
        comboBoxShoppingNameList.setVisible(false);
        btnSeeShoppingList.setVisible(false);
        Vector<Product> productOfShoppingList = new Vector<>(shoppingList);
        tableViewDisplayProductList.setItems(FXCollections.observableArrayList(productOfShoppingList));
        isVisibleElementToModifyMyShoppingList(true);
        returnToMenu.setOnAction(event -> listener.cancelRecipeCreation());
        btnConfirm.setOnAction(event -> {
            fillShoppingListToSend();
            listener.returnAddedProducts();
        });

        btnExportShoppingList.setVisible(false);
        btnSendMail.setVisible(false);
    }

    public void helpShoppingList(){
        listener.helpModifyShoppingList();
    }


}