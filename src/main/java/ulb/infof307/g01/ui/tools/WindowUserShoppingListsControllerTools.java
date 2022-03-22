package ulb.infof307.g01.ui.tools;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.ShoppingList;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.menu.WindowUserMenuListController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Super Classe contenant les methodes doublons qu'utilise la fenetre creation/modif liste de courses
 */

public class WindowUserShoppingListsControllerTools extends Window {
    protected SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
    @FXML
    protected HBox hBoxToCreateProduct;
    @FXML
    protected TableColumn columnQuantityOrNumber,columnDelete,columnProduct, columnUnity;
    @FXML
    protected ComboBox<String> comboBoxShoppingNameList;
    @FXML
    protected ComboBox<String> comboBoxListUnity;
    @FXML
    protected ComboBox<String> comboBoxListProduct;
    @FXML
    protected Button btnConfirm, btnSeeShoppingList, btnAddNewProduct;
    @FXML
    protected Spinner<Integer> spinnerQuantityOrNumber;
    @FXML
    protected TableView tableViewDisplayProductList;
    @FXML
    protected Button returnToMenu;
    protected ArrayList<String> allUnitName = null;
    protected ArrayList<String> allProduct = null;
    protected ArrayList<String> allShoppinListName = null;
    protected String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
    protected String currentShoppingListname;

    protected void removeBorderColor() {
        this.setNodeColor(tableViewDisplayProductList,false);
        this.setNodeColor(hBoxToCreateProduct, false);
    }

    public void confirmMyCreateShoppingList(){}

    /**
     * Inialise les ComboBox avec les elements de la bdd dans une liste : produit, unité, nom de liste de courses
     */
    public void initShoppingListElement() {
        try {
            allProduct = this.applicationConfiguration.getCurrent().getDatabase().getAllProductName();
            allUnitName = this.applicationConfiguration.getCurrent().getDatabase().getAllUniteName();
            allUnitName.removeAll(List.of(unitToRemove));
            allShoppinListName = this.applicationConfiguration.getCurrent().getDatabase().getAllShoppingListName();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retour au menu precedent : le menu principal de la liste de courses
     * @throws IOException
     */
    @FXML
    public void returnShoppingList() throws IOException {
        this.loadFXML("HomeShoppingList.fxml");

    }

    /**
     * Retour au menu precedent : ShowMenu
     */
    @FXML
    public void returnToMyMenu() {
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    protected void fillShoppingListToSend(ShoppingList shoppingListToSend) {
        // ajout de chaque produit de la table dans une nvl shoppingList
        for (int i = 0; i < tableViewDisplayProductList.getItems().size(); i++) {
            Product product = (Product) tableViewDisplayProductList.getItems().get(i);
            shoppingListToSend.add(product);
        }
    }

    /**
     * Empeche d'ecrire autre chose qu'un int dans le textField
     * @param newValue : string recup du textField
     */
    protected void OnlyIntOrFloatTextFieldUnity(String newValue) {
        if (!newValue.matches("^\\d*")) {
            spinnerQuantityOrNumber.getEditor().setText("0");
            spinnerValueFactory.setValue(0);
        }
    }

    /**
     * Rajoute les elements (produit, quantite, unite) choisis par l'utilisateur dans le tableView
     */
    @FXML
    public void addElementOfListToComboBoxProduct() {
        this.removeBorderColor();
        //Recuper les elements choisi pour un produit
        Object nameProductChoose = comboBoxListProduct.getSelectionModel().getSelectedItem();
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();
        Product myProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            //Cree le produit pour le mettre dans le tableView
            myProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());
            tableViewDisplayProductList.getItems().add(myProduct);

            //Nettoyer les combobox et le spinner
            comboBoxListProduct.getSelectionModel().clearSelection();
            comboBoxListUnity.getSelectionModel().clearSelection();
            spinnerValueFactory.setValue(0);
        } else {
            setNodeColor(hBoxToCreateProduct,true);
        }
    }

    public void initComboBox() {
        comboBoxListProduct.setItems(FXCollections.observableArrayList(allProduct));
        comboBoxListUnity.setItems(FXCollections.observableArrayList(allUnitName));
    }


}
