package ulb.infof307.g01.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.ShoppingList;
import ulb.infof307.g01.db.*;

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
    private Parent root;
    private Stage stage;

    public void setDatabase(Database db) {
        dataBase = db;
    }

    protected void removeBorderColor() {
        tableViewDisplayProductList.setStyle("");
        hBoxToCreateProduct.setStyle("");
    }

    public void confirmMyCreateShoppingList(){}

    /**
     * Inialise les ComboBox avec les elements de la bdd dans une liste : produit, unité, nom de liste de courses
     */
    public void initShoppingListElement() {
        try {
            allProduct = dataBase.getAllProductName();
            allUnitName = dataBase.getAllUniteName();
            allUnitName.removeAll(List.of(unitToRemove));
            allShoppinListName = dataBase.getAllShoppingListName();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retour au menu precedent : le menu principal de la liste de courses
     * @param event relier au bouton return
     * @throws IOException
     */
    @FXML
    public void returnShoppingList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowHomeShoppingListController.class.getResource("interface/FXMLMainShoppingList.fxml"));
        Parent root = loader.load();
        WindowHomeShoppingListController controller = loader.getController();
        controller.setDataBase(dataBase);

        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Retour au menu precedent : ShowMenu
     * @param event : lie au bouton return
     */
    @FXML
    public void returnToMyMenu(ActionEvent event) throws IOException {
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.setDatabase(dataBase);
        menusController.displayMyMenus(event);
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
     * Remplis les combobox des elements recup de la dataBase
     * @param comboBoxNameList combobox produit, unite et nom de liste de courses
     * @param numberOfComboBox int pour le switch case pour savoir quel combobox remplir
     */
    protected void fillComboBoxShoppingNameListWithBDD(ComboBox comboBoxNameList, int numberOfComboBox) {
        List<String> myListComboBox = new ArrayList<String>();
        switch (numberOfComboBox) {
            case 1 -> {
                myListComboBox.addAll(allShoppinListName);
            }
            case 2 -> { // PRODUIT
                myListComboBox.addAll(allProduct);
            }
            case 3 -> { // Unité
                myListComboBox.addAll(allUnitName);
            }
            default -> {
                System.out.println("Rechercher à avoir un numberOfComboBox entre 1 à 3");
            }
        }
        comboBoxNameList.setItems(FXCollections.observableArrayList(myListComboBox));
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
            hBoxToCreateProduct.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
    }

    public void initComboBox() {
        fillComboBoxShoppingNameListWithBDD(comboBoxListProduct, 2);
        fillComboBoxShoppingNameListWithBDD(comboBoxListUnity, 3);
    }


}
