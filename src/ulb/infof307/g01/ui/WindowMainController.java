package ulb.infof307.g01.ui;

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
 * La classe WindowMainController représente le controlleur
 * pour la page principale de l'application qui s'affiche
 * lorsque le programme est lancé. Elle permet à l'utilisateur d'être redirigé vers
 * la page des Listes de Courses, Recettes ou Menus.
 * @see ulb.infof307.g01.cuisine.ShoppingList
 * @see ulb.infof307.g01.cuisine.Recipe
 * @see ulb.infof307.g01.cuisine.Menu
 * */
public class WindowMainController {

    private Database dataBase;
    private String dataBaseName = "autochef.sqlite";

    /**
     * Crée une nouvelle base de données qui sera initialisée
     * dans la classe Database.
     * @see Database
     * @see ulb.infof307.g01.cuisine.Menu
     * */
    public WindowMainController() {
        dataBase = new Database(dataBaseName);
    }



    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * @throws IOException : Si le fichier FXMLMainPage n'existe pas
     * */
    @FXML
    public void displayMain(Stage primaryStage)throws IOException{
        URL ressource = getClass().getResource("interface/FXMLMainPage.fxml");
        Parent root = FXMLLoader.load(Objects.requireNonNull(ressource));
        Scene scene =  new Scene(root);
        primaryStage.setTitle("Page principale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Affiche la page principale des Listes de Courses.
     * @see WindowsMainShoppingListController
     * @throws IOException : Si le fichier FXMLMainShoppingList n'existe pas
     * */
    @FXML
    public void redirectToShoppingList(ActionEvent event) throws IOException {
        WindowsMainShoppingListController windowsShoppingListController = new WindowsMainShoppingListController();
        windowsShoppingListController.setDataBase(dataBase);
        windowsShoppingListController.displayMenuShoppingListController(event);
    }

    /**
     * Affiche la page principale des Menus.
     * @see WindowMainMenuController
     * @throws IOException : Si le fichier FXMLMainMenu n'existe pas
     * */
    @FXML
    public void redirectToMenu(ActionEvent event) throws IOException{
        WindowMainMenuController mainMenuController = new WindowMainMenuController();
        mainMenuController.setDataBase(dataBase);
        mainMenuController.displayMainMenuController(event);
    }

    /**
     *TODO: Doc pour la méthode de la page d'Aissa
     * */
    @FXML
    public void redirectRecipe(ActionEvent event) throws IOException, SQLException {
        CreateMenuController createMenuController = new CreateMenuController();
        createMenuController.displayEditMeal(event);
    }
}
