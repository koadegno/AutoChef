package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.db.*;

import java.io.IOException;

public class MainController {

    private Parent root;
    private Stage stage;
    private Scene scene;
    private Database dataBase;
    private String dataBaseName = "autochef.sqlite";

    public MainController() {
        dataBase = new Database(dataBaseName);
    }


    @FXML
    public void redirectToShoppingList(ActionEvent event) throws IOException {
        WindowsMainShoppingListController windowsShoppingListController = new WindowsMainShoppingListController();
        windowsShoppingListController.setDataBase(dataBase);
        windowsShoppingListController.displayMenuShoppingListController(event);


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
