package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.stage.Stage;
import ulb.infof307.g01.db.*;

import java.io.IOException;

public class WindowsMainShoppingListController {
    private Stage stage;
    private Parent root;
    private static Database dataBase = null;


    public void displayMenuShoppingListController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLMainShoppingList.fxml"));
        root = loader.load();
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void displayMyShoppingListController(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLMyShoppingLists.fxml"));
        root = loader.load();
        WindowsMyShoppingListsController windowsMyShoppingListsController = loader.getController();
        windowsMyShoppingListsController.setDatabase(dataBase);
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();

        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void displayCreateShoppingListController(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLCreateMyShoppingList.fxml"));
        root = loader.load();
        WindowsCreateMyShoppingListController windowsCreateMyShoppingListController = loader.getController();
        windowsCreateMyShoppingListController.setDatabase(dataBase);
        windowsCreateMyShoppingListController.initShoppingListElement();
        windowsCreateMyShoppingListController.initComboBox();

        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void returnMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load((getClass().getResource("interface/FXMLMainPage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setDataBase(Database db) {
        dataBase = db;
    }
}
