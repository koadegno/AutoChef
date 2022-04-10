package ulb.infof307.g01.ui.shoppingList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.ui.recipe.WindowCreateRecipeController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WindowUserShoppingListsController extends WindowUserShoppingListsControllerTools implements Initializable {
    private Vector<Product> myVectorProduct = new Vector<>();

    @FXML
    Button btnConfirm, btnAddNewProduct;
    private WindowCreateRecipeController callerClass=null;

    /**
     * Permet d'afficher sur le tableView les listes de courses a partir d'un nom d'une
     * liste de courses
     */
    @FXML
     public void seeMyShoppingListTableView() {
         Object nameMyShoppingList =  comboBoxShoppingNameList.getSelectionModel().getSelectedItem();

         if(Objects.equals(nameMyShoppingList, null)){ //nom est null
             isVisibleElementToModifyMyShoppingList(false);
         }
         else{
             currentShoppingListname = (String) nameMyShoppingList;
             try { // afficher les produits de la liste de course dans la table
                 ShoppingList shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListname);
                 Vector<Product> productOfShoppingList =  new Vector<>(shoppingList);
                 tableViewDisplayProductList.setItems(FXCollections.observableArrayList(productOfShoppingList));
                 isVisibleElementToModifyMyShoppingList(true);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

    }

    /**
     * Permet d'enregistrer une liste de courses que l'utilisateur aurait modifie
     * @param event : Methode liee a au bouton confirmBtn
     */
    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        try {
            //Recupere liste de courses chez la bdd
            ShoppingList shoppingListInDataBase = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListname);
            ShoppingList shoppingListToSend = new ShoppingList(shoppingListInDataBase.getName(), shoppingListInDataBase.getId());

            //Renvoie liste de courses chez la bdd
            fillShoppingListToSend(shoppingListToSend);
            Configuration.getCurrent().getShoppingListDao().update(shoppingListToSend);


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
        btnExportShoppingList.setVisible(isVisible);
        btnSendMail.setVisible(isVisible);
    }

    /**
     * Inialise les elements du fichier FXML
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activeElementVisibility();

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
            try {
                returnShoppingList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void activeElementVisibility() {
        comboBoxShoppingNameList.setVisible(true);
        btnConfirm.setText("Enregistrer");
        btnSeeShoppingList.setVisible(true);
        btnSeeShoppingList.setOnAction(e-> {seeMyShoppingListTableView();});
    }

    @Override
    public void initComboBox(){
        super.initComboBox();
        comboBoxShoppingNameList.setItems(FXCollections.observableArrayList(allShoppinListName));
    }

    public void setCallerClass(WindowCreateRecipeController windowCreateRecipeController) {
        this.callerClass = windowCreateRecipeController;
    }

    public void initForCreateRecipe(ShoppingList shoppingList) { //TODO: reformer
        super.initComboBox();
        comboBoxShoppingNameList.setVisible(false);
        btnSeeShoppingList.setVisible(false);
        Vector<Product> productOfShoppingList = new Vector<>(shoppingList);
        tableViewDisplayProductList.setItems(FXCollections.observableArrayList(productOfShoppingList));
        isVisibleElementToModifyMyShoppingList(true);
        returnToMenu.setOnAction(event -> {
            this.callerClass.cancel();
        });
        btnConfirm.setOnAction(event -> {
            ShoppingList shoppingListToReturn = new ShoppingList("current");
            fillShoppingListToSend(shoppingListToReturn);
            this.callerClass.add(shoppingListToReturn);
        });

        btnExportShoppingList.setVisible(false);
        btnSendMail.setVisible(false);
    }
}
