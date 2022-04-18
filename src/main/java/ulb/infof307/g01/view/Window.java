package ulb.infof307.g01.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.infof307.g01.model.db.Configuration;

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
        return loader.getController();
    }


    public void loadFXML(Window controller, String filename) {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(Window.class.getResource(filename)));
        loader.setController(controller);
        setNewScene(loader);
    }

    protected Stage popupFXML(String filename, Window controller) throws IOException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(Window.class.getResource(filename)));
        loader.setController(controller);
        popup.setScene(new Scene(loader.load()));
        popup.show();
        return popup;
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

    /**
     * Affiche une fenêtre Dialogue avec un titre et du texte
     * Utiliser pour les erreurs ou les messages simple à l'utilisateur
     * @param alertType le type de Dialogue
     * @param headerText l'entête, titre
     * @param contentText le texte a afficher
     * @return le type de bouton cliquer par l'utilisateur
     */
    public static ButtonType showAlert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> alertResult = alert.showAndWait();
        return alertResult.get();
    }

    @FXML
    protected void onlyIntValue(Spinner<Integer> numberOfPersonSpinner){
        numberOfPersonSpinner.getEditor().textProperty().addListener(new ChangeListener<String>() { //Seulement écrire des nombres
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfPersonSpinner.getEditor().setText(newValue.replaceAll("[^\\d*]", ""));
                }
            }
        });
    }

}
