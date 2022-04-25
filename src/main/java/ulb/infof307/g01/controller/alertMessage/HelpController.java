package ulb.infof307.g01.controller.alertMessage;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.alertMessage.HelpViewController;

import java.io.IOException;


public class HelpController extends Controller implements HelpViewController.Listener {
    private HelpViewController helpViewController;
    private Stage helpShopPopup;
    private int numberImageInformation = 1;
    private int lastNumberImageInformation;
    private String directoryImageInformation;

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
        try {
            helpShopPopup = this.popupFXML("Help.fxml", helpViewController);
            helpShopPopup.setResizable(false);
            helpViewController.setListener(this);
            createFilePathImageInformation();
        } catch (IOException e) {
            e.printStackTrace();
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
