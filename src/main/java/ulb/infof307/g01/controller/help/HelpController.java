package ulb.infof307.g01.controller.help;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.help.HelpViewController;

import java.io.IOException;

/**
 * Classe qui contrôle la section d'aide
 * Permet d'avoir un affichage de différentes photos, expliquant comment
 * fonctionne l'interface actuellement affichée
 */
public class HelpController extends Controller implements HelpViewController.Listener {
    private final HelpViewController helpViewController;
    private Stage helpShopPopup;
    private int numberImageInformation = 1;
    private final int lastNumberImageInformation;
    private final String directoryImageInformation;

    /**
     * Permet d'init les attributs et de créer la View de help
     * @param directoryImageInformation contient le nom du dossier ou se trouve les images
     * @param lastNumberImageInformation le numéro de la dernière image d'information
     */
    public HelpController(String directoryImageInformation, int lastNumberImageInformation){
        this.directoryImageInformation = directoryImageInformation;
        this.lastNumberImageInformation = lastNumberImageInformation; //le nombre d'images
        this.helpViewController = new HelpViewController();
    }

    /**
     * Affiche la popup d'aide avec la première image d'information
     */
    public void displayHelpShop(){
        String filename = "Help.fxml";
        try {
            helpShopPopup = this.popupFXML(filename, helpViewController);
            helpShopPopup.setResizable(false);
            helpViewController.setListener(this);
            createFilePathImageInformation();
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(filename);
        }
    }

    /**
     * Revoir l'image d'information précédente
     */
    @Override
    public void leftButtonClicked() {
        if((numberImageInformation - 1) > 0){
            numberImageInformation -= 1;
            createFilePathImageInformation();
        }
    }

    /**
     * Voir la prochaine image d'information
     */
    @Override
    public void rightButtonClicked() {
        if((numberImageInformation + 1) <= lastNumberImageInformation){
            numberImageInformation += 1;
            createFilePathImageInformation();
        }
        else{
            helpShopPopup.close();
        }
    }

    /**
     * Crée le début de chemin avec le dossier et le nom de l'image à afficher
     */
    public void createFilePathImageInformation(){
        String filepath = directoryImageInformation + numberImageInformation + ".png";
        helpViewController.displayImageInformation(filepath);

    }
}
