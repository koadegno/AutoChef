package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class WindowMainController {

    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    public void displayMain(Stage primaryStage){
        try{
            URL ressource = getClass().getResource("interface/FXMLMainPage.fxml");
            Parent root = FXMLLoader.load(Objects.requireNonNull(ressource));
            Scene scene =  new Scene(root);
            primaryStage.setTitle("Page principale");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @FXML
    public void redirectToShoppingList(ActionEvent event) throws IOException {
        WindowsMainShoppingListController windowsShoppingListController = new WindowsMainShoppingListController();
        windowsShoppingListController.displayMenuShoppingListController(event);

    }

    @FXML
    public void redirectMenuList(ActionEvent event) throws IOException {
        WindowMyMenusController windowMyMenus = new WindowMyMenusController();
        windowMyMenus.displayMenuList(event);
    }

    @FXML
    public void redirectRecipe(ActionEvent event) throws IOException{
        SearchRecipeController displayRecipe =  new SearchRecipeController();
        displayRecipe.displaySearchRecipe(event);

    }
}
