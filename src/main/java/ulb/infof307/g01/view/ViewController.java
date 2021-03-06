package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.control.Spinner;

import java.io.File;
import java.net.URL;
import java.util.Optional;

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
        alert.setContentText("Veuillez contactez l'équipe de développeur.");
        alert.showAndWait();
    }

    /**
     * Permet d'écrire seulement des nombres en int dans un spinner
     * @param numberOfPersonSpinner le spinner de la View
     */
    @FXML
    protected void onlyIntValue(Spinner<Integer> numberOfPersonSpinner){
        //Seulement écrire des nombres
        numberOfPersonSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfPersonSpinner.getEditor().setText(newValue.replaceAll("[^\\d*]", ""));
            }
        });
    }

    /**
     * Permet d'écrire seulement des nombres en int dans un textfield
     * @param textField TextField qui contient un string ou un int
     */
    @FXML
    protected void onlyIntValue(TextField textField){
        //Seulement écrire des nombres
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d*]", ""));
            }
        });
    }

    /**
     * Permet d'afficher l'explorateur de fichiers pour choisir un fichier
     * @param windowTitle le titre qu'aura la fenêtre
     * @param extensionDescription la description de l'extension choisie
     * @param extension l'extension du fichier qu'il est possible de choisir
     * @param ownerWindow la fenêtre
     * @return retourne le dialog de la fenêtre
     */
    public static File showFileChooser(String windowTitle, String extensionDescription,
                                       String extension, Window ownerWindow) {

        FileChooser dialog = new FileChooser();
        dialog.setTitle(windowTitle);

        final FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(extensionDescription, extension);
        dialog.getExtensionFilters().setAll(extensionFilter);

        return dialog.showOpenDialog(ownerWindow);
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

        if(alertResult.isEmpty()) return null;
        return alertResult.get();
    }


}
