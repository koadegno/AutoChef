package ulb.infof307.g01.view;

import javafx.application.Application;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.MainController;

public class Autochef extends Application {

    public  void launchApp(String[] args){
        this.launch(args);
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
