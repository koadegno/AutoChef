package ulb.infof307.g01.ui.mail;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Configuration;
import ulb.infof307.g01.ui.Window;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.SQLException;

public class FavoriteMailView extends Window {
    public VBox vBox;
    public TextField newFavoriteMail;
    private ComboBox<String> comboboxListFavoriteMaiL;


    public void confirmMail() {
        setNodeColor(newFavoriteMail, false);
        String newMail = newFavoriteMail.getText(); //envoyer ça
        if(isValidEmailAddress(newMail)){
            comboboxListFavoriteMaiL.setValue(newMail); //TODO: changer solution poubelle
            comboboxListFavoriteMaiL.getItems().add(newMail);
            //comboboxListFavoriteMaiL.setPromptText(newMail);
            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.close();
        }
        else{
            setNodeColor(newFavoriteMail, true);
        }

    }

    public void addFavoriteMail(){
        String newMail = newFavoriteMail.getText();
        setNodeColor(newFavoriteMail, false);
        if(isValidEmailAddress(newMail)){
            try {
                Configuration.getCurrent().getMailAddressDao().insert(newMail);
                confirmMail(); //TODO: changer maybe
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            setNodeColor(newFavoriteMail, true);
        }
    }

    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void setComboboxListFavoriteMail(ComboBox<String> comboboxListFavoriteMaiL){
        this.comboboxListFavoriteMaiL = comboboxListFavoriteMaiL;
    }


}
