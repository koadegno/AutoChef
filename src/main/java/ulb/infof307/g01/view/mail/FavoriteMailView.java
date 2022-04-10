package ulb.infof307.g01.view.mail;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.Window;

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
            addMailToCombobox(newMail);
            closePopup();
        }
        else{
            setNodeColor(newFavoriteMail, true);
        }

    }

    private void addMailToCombobox(String newMail) {
        comboboxListFavoriteMaiL.getItems().add(newMail);
        comboboxListFavoriteMaiL.setValue(newMail);}

    public void addFavoriteMail(){
        String newMail = newFavoriteMail.getText();
        setNodeColor(newFavoriteMail, false);
        if(isValidEmailAddress(newMail)){
            try {
                Configuration.getCurrent().getMailAddressDao().insert(newMail);
                addMailToCombobox(newMail);
                closePopup();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            setNodeColor(newFavoriteMail, true);
        }
    }

    private void closePopup(){
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }

    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress addressEmail = new InternetAddress(email);
            addressEmail.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void setComboboxListFavoriteMail(ComboBox<String> comboboxListFavoriteMaiL){
        this.comboboxListFavoriteMaiL = comboboxListFavoriteMaiL;
    }


}
