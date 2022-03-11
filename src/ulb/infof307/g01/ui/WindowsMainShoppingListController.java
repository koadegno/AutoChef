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
import java.util.Objects;

public class WindowsMainShoppingListController {
    private Stage stage;
    private Parent root;
    private static Database dataBase = null;


    public void displayMenuShoppingListController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLMainShoppingList.fxml"));
        root = loader.load();

        if( dataBase == null){System.out.println("je suis null haha 4");}

        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void displayMyShoppingListController(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLMyShoppingLists.fxml"));
        root = loader.load();
        WindowsMyShoppingListsController windowsMyShoppingListsController = loader.getController();
        if( dataBase == null){System.out.println("je suis null haha 3");}
        windowsMyShoppingListsController.setDatabase(dataBase);
        windowsMyShoppingListsController.initShoppingListElement();
        windowsMyShoppingListsController.initComboBox();


        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void displayCreateShoppingListController(ActionEvent event) throws IOException{
        root = FXMLLoader.load((getClass().getResource("interface/FXMLCreateMyShoppingList.fxml")));
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void returnMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load((getClass().getResource("interface/Main.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setDataBase(Database db) {
        if( db == null){System.out.println("je suis null haha");}
        dataBase = db;
    }
}
