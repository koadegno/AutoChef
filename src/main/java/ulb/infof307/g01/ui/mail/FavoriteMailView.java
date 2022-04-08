package ulb.infof307.g01.ui.mail;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.ui.Window;

public class FavoriteMailView extends Window {
    public VBox vBox;
    public TextField newFavoriteMail;
    private ComboBox<String> comboboxListFavoriteMaiL;


    public void confirmFavoriteMail() {
        //TODO:ajouter le mail a la base de donnee
        //TODO:verifier si c'est bien écrit
        String newMail = newFavoriteMail.getText(); //envoyer ça
        comboboxListFavoriteMaiL.setPromptText(newMail); //TODO: changer solution poubelle
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }

    public void setComboboxListFavoriteMail(ComboBox<String> comboboxListFavoriteMaiL){
        this.comboboxListFavoriteMaiL = comboboxListFavoriteMaiL;
    }


}
