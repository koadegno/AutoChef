package ulb.infof307.g01.view.mail;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.MailController;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.Window;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.SQLException;

public class FavoriteMailView extends Window {
    public VBox vBox;
    public TextField newFavoriteMail;
    private MailController mailController;

    public void setMailController(MailController mailController){
        this.mailController = mailController;
    }

    public void confirmMail() {
        String newMail = newFavoriteMail.getText(); //envoyer ça
        mailController.confirmMail(newMail);
    }

    public void addFavoriteMail(){
        String newMail = newFavoriteMail.getText();
        mailController.addFavoriteMail(newMail);
    }

    public void closePopup(){
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }


}
