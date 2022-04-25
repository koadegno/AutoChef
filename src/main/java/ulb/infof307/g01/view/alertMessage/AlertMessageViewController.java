package ulb.infof307.g01.view.alertMessage;

import javafx.scene.control.Label;
import ulb.infof307.g01.view.ViewController;

public class AlertMessageViewController extends ViewController<AlertMessageViewController.Listener> {

    public Label alertMessageLabel;

    public void closePopupAlertMessage(){
        listener.closePopupAlertMessage();
    }

    public void setAlertMessageLabel(String messageLabel){
        listener.showAlertMessagePopup();
        alertMessageLabel.setText(messageLabel);
    }

    public interface Listener{
        void closePopupAlertMessage();
        void showAlertMessagePopup();
    }
}
