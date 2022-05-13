package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.connexion.LoginController;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Classe générique de contrôleur contient
 * les méthodes de base pour tout contrôleur
 */
public abstract class Controller {

    protected static Stage currentStage;

    protected static final String dataBaseName = "autochef.sqlite";

    protected ListenerBackPreviousWindow listenerBackPreviousWindow;

    protected Controller(){ this(null); }

    protected Controller(ListenerBackPreviousWindow listenerBackPreviousWindow){
        Configuration.getCurrent().setDatabase(dataBaseName);
        this.listenerBackPreviousWindow = listenerBackPreviousWindow;
    }

    /**
     * Lance le fichier FXML
     * @param filename le fichier FXML à lancer
     * @return un FXMLLoader
     */
    public FXMLLoader loadFXML(String filename) {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(ViewController.class.getResource(filename)));
        setNewScene(loader);
        return loader;
    }


    /**
     * Lance le fichier FXML et lui attribue un contrôleur
     * @param controller le contrôleur à associer au fichier FXML
     * @param filename le nom du fichier FXML
     * @return un FXMLLoader
     */
    public FXMLLoader loadFXML(ViewController controller, String filename) {
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(ViewController.class.getResource(filename)));
        loader.setController(controller);
        setNewScene(loader);

        return loader;
    }

    /**
     * Lance le fxml d'une pop up
     * @param filename le nom du fichier fxml
     * @param controller le contrôleur associé à la pop up
     * @return javafx stage
     * @throws IOException erreur lors de l'ouverture du fichier FXML
     */
    protected Stage popupFXML(String filename, ViewController controller) throws IOException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        System.out.println("Mon erreur : " + ViewController.class.getResource(filename));
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(ViewController.class.getResource(filename)));
        loader.setController(controller);
        popup.setScene(new Scene(loader.load()));
        popup.show();

        return popup;
    }


    public void setStage(Stage stage){
        if (currentStage == null){
            currentStage = stage;
        }
    }

    public void setNewScene(FXMLLoader loader, String title) {
        Parent root;
        try {
            root = loader.load();
            currentStage.setTitle("Autochef - " + title);
            currentStage.setScene(new Scene(root));
            currentStage.show();
        }
        catch (IOException e) {
            URL missingFXMLFile = loader.getLocation();
            ViewController.showErrorFXMLMissing(missingFXMLFile);
        }


    }

    public void setNewScene(FXMLLoader loader) {
        setNewScene(loader, "");
    }

    /**
     * importe un fichier json
     * @param windowTitle le fichier a importé
     * @return Le fichier json
     */
    public File importJSON(String windowTitle){
        String extensionDescription = "Fichier JSON";
        File jsonFile = ViewController.showFileChooser(windowTitle, extensionDescription,
                "*.json", currentStage);

        if (jsonFile != null && jsonFile.getName().endsWith(".json")) {
            return jsonFile;

        }
        return null;
    }

    /**
     * Déconnecte l'utilisateur
     */
    protected void userLogout() {
        Configuration.getCurrent().setCurrentUser(null);
        LoginController loginController = new LoginController(currentStage);
        loginController.displayHomeLogin();
    }

}