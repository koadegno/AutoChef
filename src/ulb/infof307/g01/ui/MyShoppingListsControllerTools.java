package ulb.infof307.g01.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Vector;

public class MyShoppingListsControllerTools {
    public static Database dataBase;
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
    public void initShoppingListElement() {

        try { //TODO gerer l'erreur
            allProduct = dataBase.getAllProductName();
            allUnitName = dataBase.getAllUniteName();
            allUnitName.removeAll(List.of(unitToRemove));
            allShoppinListName = dataBase.getAllShoppingListName();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void returnShoppingList(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(WindowsMainShoppingListController.class.getResource("interface/FXMLMainShoppingList.fxml"));
        Parent root = loader.load();
        WindowsMainShoppingListController controller = loader.getController();
        controller.setDataBase(dataBase);

        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void returnToMyMenu(ActionEvent event) throws IOException {
        WindowMyMenusController menusController = new WindowMyMenusController();
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

    protected void OnlyIntOrFloatTextFieldUnity(String newValue) {
        if (!newValue.matches("^\\d*")) {
            spinnerQuantityOrNumber.getEditor().setText("0");
            spinnerValueFactory.setValue(0);
        }
    }
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


    @FXML
    public void addElementOfListToComboBoxProduct() {
        this.removeBorderColor();
        Object nameProductChoose = comboBoxListProduct.getSelectionModel().getSelectedItem();
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();
        Product myProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            myProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());
            tableViewDisplayProductList.getItems().add(myProduct);

            //Nettoyer les combobox et le spinner
            comboBoxListProduct.getSelectionModel().clearSelection();
            comboBoxListUnity.getSelectionModel().clearSelection();
            spinnerValueFactory.setValue(0);
        } else {
            System.out.println((String)nameProductChoose +" " +  quantityOrNumberChoose+ " " + (String)nameUnityChoose);
            hBoxToCreateProduct.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
    }

    public void initComboBox() {
        fillComboBoxShoppingNameListWithBDD(comboBoxListProduct, 2);
        fillComboBoxShoppingNameListWithBDD(comboBoxListUnity, 3);
    }


}
