package ulb.infof307.g01.ui.mail;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

public class MailView extends Window {

    @FXML
    public Label nameShoppingList;
    @FXML
    public TextField mailReceiver, subject;
    @FXML
    public TextArea messageBody;
    @FXML
    public VBox vBox;
    private ShoppingList shoppingList;

    public void setShoppingListToMail(ShoppingList shoppingList){
        this.shoppingList = shoppingList;
        nameShoppingList.setText("Liste de courses : "+shoppingList.getName());
    }

    @FXML
    public void sendMail(){
        setNodeColor(mailReceiver, false);
        try {
            Mail mail = new Mail();
            mail.sendMail(mailReceiver.getText(),shoppingList,subject.getText(), messageBody.getText());
            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.close();

        } catch (AddressException e) {
            setNodeColor(mailReceiver, true);
        } catch (MessagingException e) {
            setNodeColor(mailReceiver, true);
        }

    }



}
