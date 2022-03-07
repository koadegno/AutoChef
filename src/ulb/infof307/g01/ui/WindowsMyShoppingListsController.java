package ulb.infof307.g01.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.ui.CreateColWithButtonDelete;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WindowsMyShoppingListsController implements Initializable {
    private Parent root;
    private Stage stage;
    private Vector<Product> myVectorProduct = new Vector<>();
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);

    @FXML
    HBox hBoxToCreateProduct;
    @FXML
    ComboBox<String> comboBoxShoppingNameList, comboBoxListUnity, comboBoxListProduct;
    @FXML
    Button btnConfirm, btnAddNewProduct;
    @FXML
    Spinner<Integer> textFieldQuantityOrNumber;
    @FXML
    TableView tableViewDisplayProductList;
    @FXML
    TableColumn columnQuantityOrNumber,columnDelete,columnProduct, columnUnity;

    @FXML
    public void returnShoppingList(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainShoppingList.fxml"));
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }
     @FXML
     public void seeMyShoppingListTableView(ActionEvent event) throws IOException {
         String nameMyShoppingList =  comboBoxShoppingNameList.getEditor().textProperty().getValue();
         if(Objects.equals(nameMyShoppingList, "")){
             //TODO:error
             isVisibleElementToModifyMyShoppingList(false);
             //TODO: faire en sorte que la liste soit vide
         }
         else{
             //TODO:call bdd
             Product myProduct = new Product("salade", 1, "unité");
             final ObservableList<Product> data = FXCollections.observableArrayList(myProduct);
             tableViewDisplayProductList.setItems(data);
             //tableViewDisplayProductList.getItems().add(myProduct);
             isVisibleElementToModifyMyShoppingList(true);
             //TODO: condition aussi pour voir si la liste existe
         }

    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        Vector<Product> myListProduct = new Vector<>();
        //TODO: comparer new et old liste pour voir si c'est a envoyer a la bdd
        for (int i=0; i < tableViewDisplayProductList.getItems().size(); i++){
            Product product = (Product) tableViewDisplayProductList.getItems().get(i);
            myListProduct.add(product);
        }
        if(myListProduct.size() > 0){
            //TODO: envoyer a la bdd avec le nom
            returnShoppingList(event);
        }
        else{
            //TODO: Supprimer de la bdd??
        }
    }
    @FXML
    public void isVisibleElementToModifyMyShoppingList(boolean isVisible){
        comboBoxListProduct.setVisible(isVisible);
        textFieldQuantityOrNumber.setVisible(isVisible);
        comboBoxListUnity.setVisible(isVisible);
        btnConfirm.setVisible(isVisible);
        btnAddNewProduct.setVisible(isVisible);
    }
         @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textFieldQuantityOrNumber.setValueFactory(spinnerValueFactory);
        textFieldQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));

        fillComboBoxShoppingNameList(comboBoxShoppingNameList, 1);
        fillComboBoxShoppingNameList(comboBoxListProduct,2);
        fillComboBoxShoppingNameList(comboBoxListUnity, 3);

        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        CreateColWithButtonDelete createColWithButtonDelete = new CreateColWithButtonDelete();
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButtonDelete.createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
    }
    @FXML
    private void OnlyIntOrFloatTextFieldUnity(String newValue){
        if (!newValue.matches("^\\d*")){
            textFieldQuantityOrNumber.getEditor().setText("0");
            spinnerValueFactory.setValue(0);
        }
    }

    private void fillComboBoxShoppingNameList(ComboBox comboBoxNameList, int numberOfComboBoxList) {
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

    private List<String> callBDDToComboBoxList(int numberOfComboBox){
        List<String> myListComboBox = new ArrayList<String>();
        switch (numberOfComboBox) {
            case 1 -> {
                myListComboBox.add("liste une");
                myListComboBox.add("liste deuc");
            }
            case 2 -> {
                myListComboBox.add("Tomate");
                myListComboBox.add("Fraise");
                myListComboBox.add("kiwi");
                myListComboBox.add("eau");
            }
            case 3 -> {
                myListComboBox.add("Kg");
                myListComboBox.add("g");
                myListComboBox.add("L");
            }
            default -> {
            }
            //TODO:ERROR?
        }
        return myListComboBox;
    }

    @FXML
    public void addElementOfListToComboBoxProduct(){
        hBoxToCreateProduct.setStyle("");
        Object nameProductChoose =  comboBoxListProduct.getSelectionModel().getSelectedItem() ;
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();
        Product myProduct;

        if(!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null)) ){
            myProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());
            tableViewDisplayProductList.getItems().add(myProduct);

            //Nettoyer les combobox et le spinner //TODO: peux mieux faire
            comboBoxListProduct.getSelectionModel().clearSelection();
            comboBoxListUnity.getSelectionModel().clearSelection();
            spinnerValueFactory.setValue(0);
            //TODO: Creer une liste? Ou attendre le bouton confirmé?
        }
        else{
            hBoxToCreateProduct.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
}}
