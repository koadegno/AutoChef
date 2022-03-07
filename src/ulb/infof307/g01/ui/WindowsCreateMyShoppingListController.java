package ulb.infof307.g01.ui;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class WindowsCreateMyShoppingListController implements Initializable {

    private Vector<Product> myListProduct = new Vector<>();
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);

    @FXML
    HBox hBoxToCreateProduct;
    @FXML
    ComboBox comboBoxListProduct, comboBoxListUnity;
    @FXML
    TextField nameMyCreateShoppingList;
    @FXML
    Spinner textFieldQuantityOrNumber;
    @FXML
    TableView tableViewDisplayProductList;
    @FXML
    TableColumn columnProduct, columnQuantityOrNumber, columnUnity, columnDelete;

    @FXML
    public void returnShoppingList(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("interface/FXMLMainShoppingList.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        String name = nameMyCreateShoppingList.getText();

        nameMyCreateShoppingList.setStyle("");
        tableViewDisplayProductList.setStyle("");

        if(Objects.equals(name, "")){nameMyCreateShoppingList.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");}
        else{
            for (int i=0; i < tableViewDisplayProductList.getItems().size(); i++){
                Product product = (Product) tableViewDisplayProductList.getItems().get(i);
                myListProduct.add(product);
            }
            if(myListProduct.size() > 0){
                //TODO: envoyer a la bdd avec le nom
                returnShoppingList(event);
            }
            else{
                tableViewDisplayProductList.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
            }
        }
    }

    @FXML
    public void addElementOfListToComboBoxProduct(){
        hBoxToCreateProduct.setStyle("");
        Object nameProductChoose =  comboBoxListProduct.getSelectionModel().getSelectedItem() ;
        int quantityOrNumberChoose = spinnerValueFactory.getValue();
        Object nameUnityChoose = comboBoxListUnity.getSelectionModel().getSelectedItem();
        Product myProduct;

        if(!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null)) ){
            myProduct = new Product(nameProductChoose.toString(), 	quantityOrNumberChoose, nameUnityChoose.toString());
            tableViewDisplayProductList.getItems().add(myProduct);

            //Nettoyer les combobox et le spinner
            comboBoxListProduct.getSelectionModel().clearSelection();
            comboBoxListUnity.getSelectionModel().clearSelection();
            spinnerValueFactory.setValue(0);
            //TODO: Creer une liste? Ou attendre le bouton confirmé?
        }
        else{
            hBoxToCreateProduct.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textFieldQuantityOrNumber.setValueFactory(spinnerValueFactory);
        textFieldQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));

        fillComboBoxShoppingList(comboBoxListProduct, callBDDToComboBoxList(true), true);
        fillComboBoxShoppingList(comboBoxListUnity, callBDDToComboBoxList(false), false);

        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        CreateColWithButtonDelete createColWithButtonDelete = new CreateColWithButtonDelete();
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButtonDelete.createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
    }

    @FXML
    private void OnlyIntOrFloatTextFieldUnity(String newValue)  {
        if (!newValue.matches("^\\d*")){
            textFieldQuantityOrNumber.getEditor().setText("0");
            spinnerValueFactory.setValue(0);
        }

}
    @FXML
    public void fillComboBoxShoppingList(ComboBox comboBoxNameList, List<String> comboBoxList, boolean isListProduct)  {
        comboBoxNameList.setItems(FXCollections.observableArrayList(comboBoxList));
        comboBoxNameList.getEditor().textProperty().addListener(observable -> {
            System.out.println(comboBoxNameList.getEditor().textProperty().getValue());
            String test = comboBoxNameList.getEditor().textProperty().getValue();
            //TODO: afficher direct qd une lettre est noté?
            //list.clear();
            List<String> myProduct = callBDDToComboBoxList(isListProduct); //TODO: remplacer par db en appelant la method en bas
            //TODO;ERROR QD ON VEUT EFFACER
            //comboBoxNameList.setItems(FXCollections.observableArrayList(myProduct));
        });
    }
    private List<String> callBDDToComboBoxList(boolean isListProduct) {
        List<String> myListShopping = new ArrayList<String>();
        if (isListProduct) {
            myListShopping.add("Tomate");
            myListShopping.add("Fraise");
            myListShopping.add("kiwi");
            myListShopping.add("eau");
        }
        else{
            myListShopping.add("Kg");
            myListShopping.add("g");
            myListShopping.add("L");
        }
        return myListShopping;
    }
}
