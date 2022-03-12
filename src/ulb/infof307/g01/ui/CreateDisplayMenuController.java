package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javax.swing.table.TableColumn;
import java.io.IOException;

public class CreateDisplayMenuController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    MenuBar daysMenuBar;


    @FXML
    public void displayEditMeal(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/CreateDisplayMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void returnMain(ActionEvent event) throws IOException {
        //TODO:  return to Elsbeth's page
        root = FXMLLoader.load(getClass().getResource("interface/FXMLMainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /*
    public void modifiedBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/??????.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    */
}