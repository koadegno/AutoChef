package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ListeMenusController {
    @FXML
    TextField nomMenu;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void afficher(ActionEvent event) throws IOException{
        String nom = nomMenu.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("interface/Menu.fxml"));
        root = loader.load();

        MenuController menuController = loader.getController();
        menuController.afficherNomMenu(nom);

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene =  new Scene(root);
        stage.setTitle("Menu "+nom);
        stage.setScene(scene);
        stage.show();

    }
}
