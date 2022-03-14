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
 * @see WindowMyMenusController
 * */
public class WindowMainMenuController {
    private Stage stage;
    private Parent root;
    private static Database dataBase = null;

    /**
     * Affiche la page principale des Menus.
     * @throws IOException : Si le fichier FXMLMainMenu n'existe pas
     */
    public void displayMainMenuController(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLMainMenu.fxml"));
        root = loader.load();
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    /**
     * Affiche la page principale de l'application.
     * @see WindowMainController
     * @throws IOException : Si le fichier FXMLMainPage n'existe pas
     * */
    public void backToMainController(ActionEvent event){
        WindowMainController mainController = new WindowMainController();
        mainController.displayMain((Stage)((Node)event.getSource()).getScene().getWindow());
    }

    /**
     * Prend la base de donnée envoyée par la page précédente
     * @see Database
     */
    public void setDataBase(Database db){dataBase = db;}

    /**
     * Affiche la page qui contient la liste des menus.
     * @see WindowMyMenusController
     * @throws IOException
     * */
    public void redirectToMyMenusController(ActionEvent event) throws IOException{
        WindowMyMenusController menusController = new WindowMyMenusController();
        menusController.setDatabase(dataBase);
        menusController.displayMyMenus(event);
    }

    /**
     * Affiche la page pour créer un menu.
     * @see WindowMyMenusController
     * @throws IOException
     * */
    public void redirectToCreateMenuController(ActionEvent event) throws IOException, SQLException {
        CreateMenuController createMenu = new CreateMenuController();
        createMenu.displayEditMeal(event);
    }


}
