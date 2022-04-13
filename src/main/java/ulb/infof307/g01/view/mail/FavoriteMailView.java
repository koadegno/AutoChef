package ulb.infof307.g01.view.mail;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.view.ViewController;

public class FavoriteMailView extends ViewController<FavoriteMailView.Listener> {
    public VBox vBox;
    public TextField newFavoriteMail;

    public void confirmMail() {
        String newMail = newFavoriteMail.getText(); //envoyer ça
        listener.saveFavoriteMail(newMail, false);
    }

    public void addFavoriteMail(){
        String newMail = newFavoriteMail.getText();
        listener.saveFavoriteMail(newMail, true);
    }

    public void showAddressMailError(boolean isError){
        setNodeColor(newFavoriteMail, isError);
    }

    public interface Listener{
        void saveFavoriteMail(String newMail, boolean isSave);
    }
}
