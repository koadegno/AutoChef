package ulb.infof307.g01;

import javafx.application.Application;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.MainController;

/**
 * Vrai classe {@code Main}, point d'entrée du programme
 */
public class Autochef extends Application {

    public  void launchApp(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage){

        try{
            MainController mainController = new MainController();
            mainController.displayMain(primaryStage);

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}
