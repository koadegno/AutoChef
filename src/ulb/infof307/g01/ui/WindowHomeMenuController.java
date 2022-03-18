package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.sql.SQLException;

/**
 * La classe WindowMainMenuController représente le controleur
 * pour la page principale des Menus. Elle permet à l'utilisateur
 * de voir ses Menus et d'en créer un nouveau
 * @see ulb.infof307.g01.cuisine.Menu
 * @see WindowHomeMenuController
 * */
public class WindowHomeMenuController extends Window {

    /**
     * Affiche la page principale des Menus.
     * @throws IOException : Si le fichier FXMLMainMenu n'existe pas
     */
    public void displayMainMenuController() {
        this.loadFXML("interface/FXMLMainMenu.fxml");
    }

    /**
     * Affiche la page principale de l'application.
     * @see WindowHomeController
     * */
    public void backToMainController(ActionEvent event){
        WindowHomeController mainController = new WindowHomeController();
        mainController.displayMain((Stage)((Node)event.getSource()).getScene().getWindow());
    }

    /**
     * Prend la base de donnée envoyée par la page précédente
     * @see Database
     */

    /**
     * Affiche la page qui contient la liste des menus.
     * @see WindowUserMenuListController
     * @throws IOException
     * */
    public void redirectToMyMenusController(ActionEvent event) throws IOException{
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    /**
     * Affiche la page pour créer un menu.
     * @see WindowUserMenuListController
     * @throws IOException
     * */
    public void redirectToCreateMenuController(ActionEvent event) throws IOException, SQLException {
        WindowCreateMenuController createMenu = new WindowCreateMenuController();
        createMenu.displayEditMeal(event);
    }


}
