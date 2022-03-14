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

public class WindowMainController extends Application {

    private Database dataBase;
    private String dataBaseName = "autochef.sqlite";
    private static Stage primaryStage;

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
        windowsShoppingListController.setDataBase(dataBase);
        windowsShoppingListController.displayMenuShoppingListController(event);

    }

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
