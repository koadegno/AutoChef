package ulb.infof307.g01.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Autochef extends Application {

    public  void launchApp(String[] args){
        this.launch(args);
    }
    @Override
    public void start(Stage primaryStage){

        try{
            WindowHomeController homepage = new WindowHomeController();
            homepage.displayMain(primaryStage);

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}
