package ulb.infof307.g01.ui;

import javafx.application.Application;
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
public class WindowMainController extends Application {

    private Database dataBase;
    private String dataBaseName = "autochef.sqlite";
    private static Stage primaryStage;

    /**
     * Crée une nouvelle base de données qui sera initialisée
     * dans la classe Database.
     * @see Database
     * @see ulb.infof307.g01.cuisine.Menu
     * */
    public WindowMainController() {
        dataBase = new Database(dataBaseName);
    }

    public void launchApp(String[] args) {launch(args);}


    @Override
    public void start(Stage primaryStage){

        try{
            this.primaryStage = primaryStage;
            displayMain(this.primaryStage);

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * @throws IOException : Si le fichier FXMLMainPage n'existe pas
     * */
    @FXML
    public void displayMain(Stage primaryStage){
        try{
            URL ressource = getClass().getResource("interface/FXMLMainPage.fxml");
            Parent root = FXMLLoader.load(Objects.requireNonNull(ressource));
            Scene scene =  new Scene(root);
            primaryStage.setTitle("Autochef");
            primaryStage.setScene(scene);
            primaryStage.show();

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
    public void redirectToMenu(ActionEvent event) throws IOException, SQLException {
        WindowMainMenuController mainMenuController = new WindowMainMenuController();
        mainMenuController.setDataBase(dataBase);
        mainMenuController.displayMainMenuController(event);
    }

    @FXML
    public void closeApplication(){
        primaryStage.close();
    }

}
