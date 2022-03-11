package ulb.infof307.g01.ui;

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
    public void redirectToShoppingList(ActionEvent event) throws IOException {
        WindowsMainShoppingListController windowsShoppingListController = new WindowsMainShoppingListController();
        windowsShoppingListController.displayMenuShoppingListController(event);

    }

    @FXML
    public void redirectMenuList(ActionEvent event) throws IOException {
        MenuController menu =  new MenuController();
        menu.back(event);

    }

    @FXML
    public void redirectRecipe(ActionEvent event) throws IOException{
        SearchRecipeController displayRecipe =  new SearchRecipeController();
        displayRecipe.displaySearchRecipe(event);

    }
}
