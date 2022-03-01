import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    public void displayMain(Stage stage)throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/Main.fxml")));
        Scene scene =  new Scene(root);
        stage.setTitle("Page d'accueil");
        stage.setScene(scene);
        stage.show();
    }


    public void redirectToShoppingList(ActionEvent e){
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        ListeDeCourseApplication menuShoppingList = new ListeDeCourseApplication(stage);
        menuShoppingList.menu(); //Menu principal des shopping lists

    }

    public void redirectMenuList(ActionEvent event) throws IOException {
        MenuListController menu = new MenuListController();
        menu.displayMenuList(event);
    }
}
