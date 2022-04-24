package ulb.infof307.g01.controller.alertMessage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.alertMessage.AlertMessageViewController;

import java.io.IOException;
import java.util.Objects;

public class AlertMessageController extends Controller implements AlertMessageViewController.Listener{
    private AlertMessageViewController alertMessageViewController;
    private Stage popupAlertMessage;

    public void displayAlertMessage(){
        this.alertMessageViewController = new AlertMessageViewController();
        this.loadPopup();
        alertMessageViewController.setListener(this);
    }

    private void loadPopup(){
        this.popupAlertMessage = new Stage();
        popupAlertMessage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader= new FXMLLoader(Objects.requireNonNull(ViewController.class.getResource("AlertMessage.fxml")));
        loader.setController(alertMessageViewController);
        try {
            popupAlertMessage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlertMessagePopup(){
        popupAlertMessage.show();
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

    public void showImportJsonError(String alertMessage){
        alertMessageViewController.setAlertMessageLabel(alertMessage);
    }
}
