package ulb.infof307.g01.view.mail;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.model.ShoppingList;
import java.util.List;

/**
 * La classe gère la vue pour l'envoie d'un mail
 */

public class MailViewController extends ViewController<MailViewController.Listener> {

    @FXML
    public Label nameShoppingList;
    @FXML
    public TextField subject;
    @FXML
    public TextArea messageBody;
    @FXML
    public VBox vBox;
    @FXML
    public ComboBox<String> mailReceiver;

    /**
     * Change le nom du label pour afficher le nom de la ShoppingList
     * @param shoppingList une liste de courses
     */
    public void showNameShoppingListToMail(ShoppingList shoppingList){
        nameShoppingList.setText("Liste de courses : "+shoppingList.getName());
    }

    /**
     * Fais appel au listener pour qu'il puisse envoyer un mail
     */
    @FXML
    public void sendMail(){
        listener.sendMail(mailReceiver.getSelectionModel().getSelectedItem(),subject.getText(), messageBody.getText());
    }

    public void chooseFavoriteMail(){
        listener.createFavoriteMail();
    }

    /**
     * Colorie en rouge la combobox qui contient la liste de mail
     * @param isError boolean qui affirme s'il y a une erreur
     */
    public void showAddressMailError(boolean isError){
        setNodeColor(mailReceiver, isError);
    }

    /**
     * Initialise la combobox de liste de mail
     * @param allMail une liste de mail
     */
    public void initComboboxFavoriteMail(List<String> allMail){
        mailReceiver.setItems(FXCollections.observableArrayList(allMail));
    }

    /**
     * Ajoute un mail dans la combobox
     * @param newMail un mail en string
     */
    public void addMailToCombobox(String newMail) {
        mailReceiver.getItems().add(newMail);
        mailReceiver.setValue(newMail);}

    public void logout() {
        listener.logout();
    }

    public void helpCreateMail(){
        listener.helpCreateMailClicked();
    }

    public interface Listener {
        void sendMail(String recipientAddress, String subject, String mailTextBody);
        void createFavoriteMail();
        void logout();
        void helpCreateMailClicked();
    }
}
