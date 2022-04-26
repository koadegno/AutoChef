package ulb.infof307.g01;

import javafx.application.Application;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.LoginController;

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
            LoginController loginController = new LoginController(primaryStage);
            loginController.displayHomeLogin();
        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}
