package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import ulb.infof307.g01.ui.menu.WindowHomeMenuController;
import ulb.infof307.g01.ui.shoppingList.WindowHomeShoppingListController;

import java.io.IOException;

/**
 * La classe WindowMainController représente le controleur
 * pour la page principale de l'application qui s'affiche
 * lorsque le programme est lancé. Elle permet à l'utilisateur d'être redirigé vers
 * la page des Listes de Courses, Recettes ou Menus.
 * @see ulb.infof307.g01.cuisine.ShoppingList
 * @see ulb.infof307.g01.cuisine.Recipe
 * @see ulb.infof307.g01.cuisine.Menu
 * */
public class WindowHomeController extends Window{
    public WindowHomeController() {}

    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * */
    @FXML
    public void displayMain(Stage primaryStage){
        try{
            this.setStage(primaryStage);
            this.loadFXML("FXMLMainPage.fxml");
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * Affiche la page principale des Listes de Courses.
     * @see WindowHomeShoppingListController
     * @throws IOException : Si le fichier FXMLMainShoppingList n'existe pas
     * */
    @FXML
    public void redirectToShoppingList(ActionEvent event) throws IOException {
        WindowHomeShoppingListController windowsShoppingListController = new WindowHomeShoppingListController();
        windowsShoppingListController.displayMenuShoppingListController();

    }

    /**
     * Affiche la page principale des Menus.
     * @see WindowHomeMenuController
     * @throws IOException : Si le fichier FXMLMainMenu n'existe pas
     * */
    @FXML
    public void redirectToMenu(){
        WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
        mainMenuController.displayMainMenuController();
    }

    @FXML
    public void closeApplication(){
        this.primaryStage.close();
    }

}
