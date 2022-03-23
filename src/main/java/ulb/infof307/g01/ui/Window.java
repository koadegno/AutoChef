package ulb.infof307.g01.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Configuration;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Window  {
    protected static Configuration applicationConfiguration;
    protected static Stage primaryStage;
    protected String dataBaseName = "autochef.sqlite";



    protected Window(){
        Configuration.getCurrent().setDatabase(dataBaseName);
    }

    public void setNodeColor(Node node, boolean isError){
        if(isError) node.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        else node.setStyle(null);
    }

    public Window loadFXML(String filename) {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(Window.class.getResource(filename)));
        setNewScene(loader);
        Window controller = loader.getController();
        return controller;
    }


    public void loadFXML(Window controller, String filename) {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(Window.class.getResource(filename)));
        loader.setController(controller);
        setNewScene(loader);
    }


    public static void setStage(Stage stage){
        if (primaryStage == null){
            primaryStage = stage;
        }
    }

    public void setNewScene(FXMLLoader loader) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Autochef");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    protected ButtonType showAlert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> alertResult = alert.showAndWait();
        return alertResult.get();
    }

}
