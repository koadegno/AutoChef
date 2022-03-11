package ulb.infof307.g01.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.ShoppingList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import static java.lang.System.exit;

public class WindowsCreateMyShoppingListController extends MyShoppingListsControllerTools implements Initializable {

    private Vector<Product> myListProduct = new Vector<>();

    @FXML
    TextField nameMyCreateShoppingList;
    @FXML
    TableView tableViewDisplayProductList;
    @FXML
    TableColumn columnProduct, columnQuantityOrNumber, columnUnity, columnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.spinnerQuantityOrNumber.setValueFactory(spinnerValueFactory);
        spinnerQuantityOrNumber.getEditor().textProperty().addListener((obs, oldValue, newValue) -> OnlyIntOrFloatTextFieldUnity(newValue));

        columnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnQuantityOrNumber.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        columnUnity.setCellValueFactory(new PropertyValueFactory<Product, String>("nameUnity"));
        CreateColWithButtonDelete createColWithButtonDelete = new CreateColWithButtonDelete();
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = createColWithButtonDelete.createColWithButton(tableViewDisplayProductList);
        columnDelete.setCellFactory(cellFactory);
    }

    @FXML
    public void confirmMyCreateShoppingList(ActionEvent event) throws IOException {

        String shoppingListName = nameMyCreateShoppingList.getText();
        if(Objects.equals(shoppingListName, "")){
            nameMyCreateShoppingList.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        }
        else {
            ShoppingList shoppingListToSend = new ShoppingList(shoppingListName);
            fillShoppingListToSend(shoppingListToSend);
            try {
                System.out.println("je veux etre ici mais j'y suis deja bg");
                dataBase.saveNewShoppingList(shoppingListToSend);
            }
            catch (SQLiteException e) {
                System.out.println("je veux etre ici");
                System.out.println(e.getErrorCode());
            } catch (SQLException e) {
                System.out.println("yoo" + e.getErrorCode());
                e.printStackTrace();
                exit(e.getErrorCode());
            }
        }
    }

}
