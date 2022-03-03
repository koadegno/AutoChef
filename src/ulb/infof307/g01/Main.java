package ulb.infof307.g01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage){


        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/Main.fxml")));
            Scene scene =  new Scene(root);
            primaryStage.setTitle("Page principale");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e ){
            e.printStackTrace();
        }



        //ListeDeCourseApplication menuShoppingList = new ListeDeCourseApplication(primaryStage);
        //menuShoppingList.menu(); //Menu principal
    }
}