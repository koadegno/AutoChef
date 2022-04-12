package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.ViewController;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public abstract class Controller {

    protected static Configuration applicationConfiguration;
    protected Stage currentStage;

    protected static String dataBaseName = "autochef.sqlite";


    protected Controller(){
        Configuration.getCurrent().setDatabase(dataBaseName);
    }

    public FXMLLoader loadFXML(String filename) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
        setNewScene(loader);
        return loader;
    }


    public FXMLLoader loadFXML(Controller controller, String filename) {
        FXMLLoader loader= new FXMLLoader(getClass().getResource(filename));
        loader.setController(controller);
        setNewScene(loader);

        return loader;
    }

    protected void popupFXML(String filename, Controller controller) throws IOException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(Controller.class.getResource(filename)));
        loader.setController(controller);
        popup.setScene(new Scene(loader.load()));
        popup.show();
    }


    public void setStage(Stage stage){
        if (currentStage == null){
            currentStage = stage;
        }
    }

    public void setNewScene(FXMLLoader loader, String title) {
        Parent root = null;
        try {root = loader.load();}
        catch (IOException e) {
            URL missingFXMLFile = loader.getLocation();
            ViewController.showErrorFXMLMissing(missingFXMLFile);
        }

        currentStage.setTitle("Autochef - " + title);
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }

    public void setNewScene(FXMLLoader loader) {
        setNewScene(loader, "");
    }

}