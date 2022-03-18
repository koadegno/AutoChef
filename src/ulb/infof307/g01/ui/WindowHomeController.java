package ulb.infof307.g01.ui;

import javafx.application.Application;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.db.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;

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
            this.loadFXML("interface/FXMLMainPage.fxml");
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * Affiche la page principale des Listes de Courses.
     * @see WindowsMainShoppingListController
     * @throws IOException : Si le fichier FXMLMainShoppingList n'existe pas
     * */
    @FXML
    public void redirectToShoppingList(ActionEvent event) throws IOException {
        WindowHomeShoppingListController windowsShoppingListController = new WindowHomeShoppingListController();
        windowsShoppingListController.setDataBase(applicationConfiguration.getCurrent().getDatabase());
        windowsShoppingListController.displayMenuShoppingListController(event);

    }

    /**
     * Affiche la page principale des Menus.
     * @see WindowMainMenuController
     * @throws IOException : Si le fichier FXMLMainMenu n'existe pas
     * */
    @FXML
    public void redirectToMenu(ActionEvent event) throws IOException, SQLException {
        WindowHomeMenuController mainMenuController = new WindowHomeMenuController();
        mainMenuController.setDataBase(applicationConfiguration.getCurrent().getDatabase());
        mainMenuController.displayMainMenuController(event);
    }

    @FXML
    public void closeApplication(){
        this.primaryStage.close();
    }

}
