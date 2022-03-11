package ulb.infof307.g01.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
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

public class MyShoppingListsControllerTools {
    public static Database dataBase;
    protected SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
    @FXML
    HBox hBoxToCreateProduct;
    @FXML
    ComboBox<String> comboBoxShoppingNameList;
    @FXML
    ComboBox<String> comboBoxListUnity;
    @FXML
    ComboBox<String> comboBoxListProduct;
    @FXML
    Spinner<Integer> textFieldQuantityOrNumber;
    @FXML
    TableView tableViewDisplayProductList;
    ArrayList<String> allUnitName = null;
    ArrayList<String> allProduct = null;
    ArrayList<String> allShoppinListName = null;
    String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
    String currentShoppingListname;
    private Parent root;
    private Stage stage;

    public void setDatabase(Database db) {
        if (db == null) {
            System.out.println("je suis null haha 2");
        }
        dataBase = db;
    }

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
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainShoppingList.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        try {
            ShoppingList shoppingListInDataBase = dataBase.getShoppingListFromName(currentShoppingListname);
            ShoppingList shoppingListToSend = new ShoppingList(shoppingListInDataBase.getName(), shoppingListInDataBase.getId());

            // ajout de chaque produit de la table dans une nvl shoppingList
            for (int i = 0; i < tableViewDisplayProductList.getItems().size(); i++) {
                Product product = (Product) tableViewDisplayProductList.getItems().get(i);
                System.out.println(product.getName());
                shoppingListToSend.add(product);
            }
            dataBase.saveModifyShoppingList(shoppingListToSend);
            returnShoppingList(event);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void OnlyIntOrFloatTextFieldUnity(String newValue) {
        if (!newValue.matches("^\\d*")) {
            textFieldQuantityOrNumber.getEditor().setText("0");
            spinnerValueFactory.setValue(0);
        }
    }

    private List<String> callBDDToComboBoxList(int numberOfComboBox) {
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
            }
            //TODO:ERROR?
        }
        return myListComboBox;
    }

    @FXML
    public void addElementOfListToComboBoxProduct() {
        hBoxToCreateProduct.setStyle("");
        Object nameProductChoose = comboBoxListProduct.getSelectionModel().getSelectedItem();
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();
        Product myProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            myProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());
            tableViewDisplayProductList.getItems().add(myProduct);

            //Nettoyer les combobox et le spinner //TODO: peux mieux faire
            comboBoxListProduct.getSelectionModel().clearSelection();
            comboBoxListUnity.getSelectionModel().clearSelection();
            spinnerValueFactory.setValue(0);
            //TODO: Creer une liste? Ou attendre le bouton confirmé?
        } else {
            hBoxToCreateProduct.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
    }

    public void fillComboBoxShoppingNameList(ComboBox comboBoxNameList, int numberOfComboBoxList) {

        comboBoxNameList.setItems(FXCollections.observableArrayList(callBDDToComboBoxList(numberOfComboBoxList)));
        comboBoxNameList.getEditor().textProperty().addListener(observable -> {
            System.out.println(comboBoxNameList.getEditor().textProperty().getValue());
            String test = comboBoxNameList.getEditor().textProperty().getValue();
            //TODO: afficher direct qd une lettre est noté?
            //list.clear();
            List<String> myProduct = callBDDToComboBoxList(numberOfComboBoxList); //TODO: remplacer par db en appelant la method en bas
            //TODO;ERROR QD ON VEUT EFFACER
            //comboBoxNameList.setItems(FXCollections.observableArrayList(myProduct));
        });
    }

    public void initComboBox() {
        fillComboBoxShoppingNameList(comboBoxShoppingNameList, 1);
        fillComboBoxShoppingNameList(comboBoxListProduct, 2);
        fillComboBoxShoppingNameList(comboBoxListUnity, 3);
    }
}
