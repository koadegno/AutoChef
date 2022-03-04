package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WindowsMainShoppingListController {
    private Stage stage;
    private Parent root;

    public void displayMenuShoppingListController(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainShoppingList.fxml"));
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    @FXML
    public void displayMyShoppingListController(ActionEvent event) throws IOException{
        root = FXMLLoader.load((getClass().getResource("interface/FXMLMyShoppingLists.fxml")));
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
}
