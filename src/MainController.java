import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    private Parent root;
    private Stage stage;
    private Scene scene;
    /*
    Need it for the return button
    private final Stage primaryStage;
    public MainController(Stage _primaryStage){
        this.primaryStage = _primaryStage;
    }
    */

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
    @FXML
    public void editMeal(ActionEvent event){

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        EditMeal meal = new EditMeal(stage);

    }

}
