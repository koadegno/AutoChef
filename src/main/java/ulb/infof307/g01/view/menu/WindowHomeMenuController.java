package ulb.infof307.g01.view.menu;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.WindowHomeController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * La classe WindowHomeMenuController représente le controleur
 * pour la page principale des Menus. Elle permet à l'utilisateur
 * de voir ses Menus et d'en créer un nouveau
 * @see ulb.infof307.g01.model.Menu
 * */
public class WindowHomeMenuController extends Window {

    /**
     * Affiche la page principale des Menus.
     */
    public void displayMainMenuController() {
        this.loadFXML("HomeMenu.fxml");
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