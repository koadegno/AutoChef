package ulb.infof307.g01.ui.menu;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Database;
import ulb.infof307.g01.ui.Window;
import ulb.infof307.g01.ui.WindowHomeController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * La classe WindowHomeMenuController représente le controleur
 * pour la page principale des Menus. Elle permet à l'utilisateur
 * de voir ses Menus et d'en créer un nouveau
 * @see ulb.infof307.g01.cuisine.Menu
 * */
public class WindowHomeMenuController extends Window {

    /**
     * Affiche la page principale des Menus.
     */
    public void displayMainMenuController() {
        this.loadFXML("FXMLMainMenu.fxml");
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
     * Affiche la page qui contient la liste des menus.
     * @see WindowUserMenuListController
     * */
    public void redirectToMyMenusController(){
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    /**
     * Affiche la page pour créer un menu.
     * @see WindowUserMenuListController
     * @throws IOException
     * */
    public void redirectToCreateMenuController() throws IOException, SQLException {
        WindowCreateMenuController createMenu = new WindowCreateMenuController();
        createMenu.displayEditMeal();
    }


}
