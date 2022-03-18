package ulb.infof307.g01.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Configuration;

import java.io.IOException;
import java.util.Objects;

public class Window  {
    protected static Configuration applicationConfiguration;
    protected static Stage primaryStage;
    protected String dataBaseName = "autochef.sqlite";



    protected Window(){
        Configuration.getCurrent().setDatabase(dataBaseName);
    }



    public Window loadFXML(String filename) throws IOException {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(getClass().getResource(filename)));
        Parent root = loader.load();
        Window controller = loader.getController();
        this.setNewScene(root);
        return controller;
    }


    public static void setStage(Stage stage){
        if (primaryStage == null){
            primaryStage = stage;
        }
    }

    public void setNewScene(Parent root){
        primaryStage.setTitle("Autochef");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
