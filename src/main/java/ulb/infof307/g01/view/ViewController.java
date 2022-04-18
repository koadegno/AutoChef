package ulb.infof307.g01.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.control.Spinner;

import java.io.File;
import java.net.URL;

/**
 * Classe de base de tous les Contrôleurs de vue
 * @param <Listener> Interface qui doit être implémentée par le Contrôleur, différente pour chaque
 *                  Contrôleur de Vue.
 */
abstract public class ViewController<Listener> {

    protected Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Colore en rouge les bords d'un élément pour signifier une erreur.
     * @param node l'élément de la vue dont il faut colorer les bords en rouge.
     * @param isError Si {@code true}, colore les bords de {@code node} en rouge,
     *                sinon reinitialisation du style de l'élément
     */
    protected void setNodeColor(Node node, boolean isError){
        if(isError) node.setStyle("-fx-border-color: #e01818 ; -fx-border-width: 2px ;");
        else node.setStyle(null);
    }

    /**
     * Affiche une alerte de type Erreur pour les fichiers FXML manquants.
     * @param missingFXMLFile L'URL du fichier manquant ayant provoqué l'erreur
     */
    public static void showErrorFXMLMissing(URL missingFXMLFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("La fenêtre n'a pas pu être chargée.");
        alert.setHeaderText("Un fichier nécessaire n'a pas pu être trouvé");
        alert.setContentText("Le fichier" + missingFXMLFile + " est manquant.\n" +
                "Veuillez contactez l'équipe de développeur.");
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de type Erreur pour les fichiers FXML manquants.
     * @param missingFXMLFile L'URL du fichier manquant ayant provoqué l'erreur
     */
    public static void showErrorFXMLMissing(String missingFXMLFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("La fenêtre n'a pas pu être chargée.");
        alert.setHeaderText("Un fichier nécessaire n'a pas pu être trouvé");
        alert.setContentText("Le fichier" + missingFXMLFile + " est manquant.\n" +
                "Veuillez contactez l'équipe de développeur.");
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de type Erreur pour les fichiers FXML manquants.
     */
    public static void showErrorSQL() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur SQL");
        alert.setHeaderText("Un problème à eu lieu avec la base de données");
        alert.setContentText("TODO"); //TODO Meilleur erreur
        alert.showAndWait();
    }

    @FXML
    protected void onlyIntValue(Spinner<Integer> numberOfPersonSpinner){
        //Seulement écrire des nombres
        numberOfPersonSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfPersonSpinner.getEditor().setText(newValue.replaceAll("[^\\d*]", ""));
            }
        });
    }

    public static File showFileChooser(String windowTitle, String extensionDescription,
                                       String extension, Window ownerWindow) {

        FileChooser dialog = new FileChooser();
        dialog.setTitle(windowTitle);

        final FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(extensionDescription, extension);
        dialog.getExtensionFilters().setAll(extensionFilter);

        return dialog.showOpenDialog(ownerWindow);
    }
}
