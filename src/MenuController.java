package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    Label nomMenu;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void afficherNomMenu(String nom){
        nomMenu.setText(nom);
    }

    public void retour(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/ListeMenus.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
