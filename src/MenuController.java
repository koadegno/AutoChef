
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MenuController {

    @FXML
    Label menuName;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void displayMenuName(String name){
        menuName.setText(name);
    }


    public void displayMenu(MouseEvent mousePressed, String name)throws IOException{
        System.out.println("Menu existe");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("interface/Menu.fxml"));
        root = loader.load();

        MenuController menuController = loader.getController();
        menuController.displayMenuName(name);

        stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
        scene =  new Scene(root);
        stage.setTitle("Menu "+name);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        MenuListController menu = new MenuListController();
        menu.displayMenuList(event);
    }
}
