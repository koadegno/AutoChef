package ulb.infof307.g01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.ui.WindowMainController;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage){

        try{
            WindowMainController mainController = new WindowMainController();
            mainController.displayMain(primaryStage);

        }catch (Exception e ){
            e.printStackTrace();
        }

    }
}