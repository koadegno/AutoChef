package ulb.infof307.g01.ui.mail;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.infof307.g01.model.Mail;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.ui.Window;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.ArrayList;

public class MailView extends Window {

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
    private ShoppingList shoppingList;

    public void setShoppingListToMail(ShoppingList shoppingList){
        this.shoppingList = shoppingList;
        nameShoppingList.setText("Liste de courses : "+shoppingList.getName());
        initComboboxFavoriteMail();
    }

    @FXML
    public void sendMail(){
        setNodeColor(mailReceiver, false);
        try {
            Mail mail = new Mail();
            mail.sendMail(mailReceiver.getSelectionModel().getSelectedItem(),shoppingList,subject.getText(), messageBody.getText());
            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.close();

        } catch (AddressException e) {
            setNodeColor(mailReceiver, true);
        } catch (MessagingException e) {
            setNodeColor(mailReceiver, true);
        }

    }

    public void chooseFavoriteMail(){
        FavoriteMailView favoriteMailView = new FavoriteMailView();
        try {
            popupFXML("favoriteMail.fxml", favoriteMailView);
            favoriteMailView.setComboboxListFavoriteMail(mailReceiver);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initComboboxFavoriteMail(){
        //TODO: recuperer le mail chez la base de donne
        ArrayList<String> allMail = fakeDataBase();
        mailReceiver.setItems(FXCollections.observableArrayList(allMail));
    }

    private ArrayList<String> fakeDataBase(){
        ArrayList<String> list = new ArrayList();
        list.add("test@gmail.com");
        list.add("hello@hotmail.be");
        return list ;
    }



}
