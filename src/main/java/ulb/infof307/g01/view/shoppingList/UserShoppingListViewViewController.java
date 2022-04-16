package ulb.infof307.g01.view.shoppingList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

public class UserShoppingListViewViewController extends ShoppingListViewController implements Initializable {
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

    /**
     * Inialise les elements du fichier FXML
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO DUPLICATE
        activeElementVisibility(true);

        this.spinnerQuantityOrNumber.setValueFactory(spinnerValueFactory);
        spinnerQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));

        //Inialise les colonne avec la classe de Product
        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));

        //Cree les boutons delete dans chaque ligne de la tableView
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);

        returnToMenu.setOnAction((event) ->{
            returnShoppingList();
        });
    }

    private void activeElementVisibility(boolean isVisible) {
        comboBoxShoppingNameList.setVisible(isVisible);
        btnConfirm.setText("Enregistrer");
        btnSeeShoppingList.setVisible(isVisible);
        btnSeeShoppingList.setOnAction(e-> {seeMyShoppingListTableView();});
    }

    public void initComboBox(ArrayList<String> allProduct, ArrayList<String> allUnitName, ArrayList<String> allShoppinListName ){
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
        returnToMenu.setOnAction(event -> {
            listener.cancelRecipeCreation();
        });
        btnConfirm.setOnAction(event -> {
            fillShoppingListToSend();
            listener.returnAddedProducts();
        });

        btnExportShoppingList.setVisible(false);
        btnSendMail.setVisible(false);
    }


}
