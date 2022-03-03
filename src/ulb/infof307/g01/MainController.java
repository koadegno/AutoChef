package ulb.infof307.g01;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    public void redirectToShoppingList(ActionEvent e){

        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        ListeDeCourseApplication menuShoppingList = new ListeDeCourseApplication(stage);
        menuShoppingList.menu(); //Menu principal

    }

    @FXML
    public void redirectMenuList(ActionEvent event) throws IOException {
        MenuController menu =  new MenuController();
        menu.back(event);

    }

    @FXML
    public void redirectRecipe(ActionEvent event){

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        MenuRecipe recipe = new MenuRecipe(stage);
        recipe.menu(); //Menu principal

    }
}
