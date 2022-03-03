package ulb.infof307.g01;

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
    Label menuName;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void displayMenuName(String name){
        menuName.setText(name);
    }

    public void back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("interface/MenuList.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
