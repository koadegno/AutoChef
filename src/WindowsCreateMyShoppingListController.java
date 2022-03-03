import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WindowsCreateMyShoppingListController implements Initializable {

    private Parent root;
    private Stage stage;
    private Vector<Product> myListProduct = new Vector<>();

    @FXML
    ComboBox comboBoxListProduct, comboBoxListUnity;
    @FXML
    TextField textFieldQuantityOrNumber, nameMyCreateShoppingList;
    @FXML
    TableView tableViewDisplayProductList;
    @FXML
    TableColumn columnProduct, columnQuantityOrNumber, columnUnity, columnDelete;

    @FXML
    public void returnShoppingList(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainShoppingList.fxml"));
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {
        String name = nameMyCreateShoppingList.getText();
        if(Objects.equals(nameMyCreateShoppingList, "")){
            //TODO: error
        }
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
                //TODO: error rien ecrit
            }
        }
    }

    @FXML
    public void addElementOfListToComboBoxProduct(){
        String nameProductChoose =  comboBoxListProduct.getEditor().textProperty().getValue();
        String quantityOrNumberChoose = textFieldQuantityOrNumber.getText();
        String nameUnityChoose = comboBoxListUnity.getEditor().textProperty().getValue();
        Product myProduct;

        if(Objects.equals(nameProductChoose, "") || Objects.equals(quantityOrNumberChoose, "") || Objects.equals(nameUnityChoose, "") ){
            //TODO:error
        }
        else{
            myProduct = new Product(nameProductChoose, 	Integer.parseInt(quantityOrNumberChoose), nameUnityChoose);
            tableViewDisplayProductList.getItems().add(myProduct);
            //TODO: effacer le texte ecrit
            //TODO: Creer une liste? Ou attendre le bouton confirmé?
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OnlyIntOrFloatTextFieldUnity();
        fillComboBoxShoppingList(comboBoxListProduct, callBDDToComboBoxList(true), true);
        fillComboBoxShoppingList(comboBoxListUnity, callBDDToComboBoxList(false), false);

        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButton();
        columnDelete.setCellFactory(cellFactory);
    }

    @FXML
    private void OnlyIntOrFloatTextFieldUnity(){
        textFieldQuantityOrNumber.textProperty().addListener(new ChangeListener<String>() { //Seulement écrire des nombres
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldQuantityOrNumber.setText(newValue.replaceAll("[^\\d*]", ""));
                }
            }
        });
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
    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createColWithButton(){
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>() {
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Product data = getTableView().getItems().get(getIndex());
                            tableViewDisplayProductList.getItems().remove(data);
                            System.out.println("selectedDataDelete: " + data.getQuantity());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                        }
                    }
                };
                return cell;
            }
        };
        return cellFactory;
    }

}
