package ulb.infof307.g01.controller.alertMessage;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.alertMessage.AlertMessageViewController;

import java.io.IOException;

public class AlertMessageController extends Controller implements AlertMessageViewController.Listener{
    private AlertMessageViewController alertMessageViewController;
    private Stage popupAlertMessage;

    public void displayAlertMessage(){
        this.alertMessageViewController = new AlertMessageViewController();
        try {
            popupAlertMessage = popupFXML("AlertMessage.fxml", alertMessageViewController);
            alertMessageViewController.setListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closePopupAlertMessage(){
        popupAlertMessage.close();
    }

    public void createShoppingListAlertMessage(){
        alertMessageViewController.setAlertMessageLabel("La liste de course a été modifiée");
    }

    public void deleteShoppingListeAlertMessage(){
        alertMessageViewController.setAlertMessageLabel("Vous avez enregistré une liste de course vide.\n" +
                " Elle est donc supprimée. ");
    }

    public void importNotWorkAlertMessage(){
        alertMessageViewController.setAlertMessageLabel("Vous n'avez pas importer un fichier JSON.");
    }
}
