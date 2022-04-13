package ulb.infof307.g01.view.mail;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.view.ViewController;

public class FavoriteMailView extends ViewController<FavoriteMailView.Listener> {
    public VBox vBox;
    public TextField newFavoriteMail;

    /**
     * Fonction relier à un bouton pour valider l'entrée d'un nouveau mail
     */
    public void confirmMail() {
        String newMail = newFavoriteMail.getText();
        listener.saveFavoriteMail(newMail, false);
    }

    /**
     * Fonction relier à un bouton pour ajouter un nouveau mail favorie
     */
    public void addFavoriteMail(){
        String newMail = newFavoriteMail.getText();
        listener.saveFavoriteMail(newMail, true);
    }

    /**
     * Colorie en rouge le textField qui contient un mail qui aurait été mal écrit
     * @param isError boolean qui affirme s'il y a une erreur
     */
    public void showAddressMailError(boolean isError){
        setNodeColor(newFavoriteMail, isError);
    }

    public interface Listener{
        void saveFavoriteMail(String newMail, boolean isSave);
    }
}
