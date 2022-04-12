package ulb.infof307.g01.view;

import javafx.scene.Node;
import javafx.scene.control.Alert;

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
     */
    public static void showErrorSQL() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur SQL");
        alert.setHeaderText("Un problème à eu lieu avec la base de données");
        alert.setContentText("TODO"); //TODO Meilleur erreur
        alert.showAndWait();
    }
}
