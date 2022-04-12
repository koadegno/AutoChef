package ulb.infof307.g01.view.mail;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.controller.MailController;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.Window;
import java.util.List;

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
    private MailController mailController;

    public void setMailController(MailController mailController){
        this.mailController = mailController;
    }

    public void setShoppingListToMail(ShoppingList shoppingList){
        nameShoppingList.setText("Liste de courses : "+shoppingList.getName());
    }

    @FXML
    public void sendMail(){
        setNodeColor(mailReceiver, false);
        mailController.sendMail(mailReceiver.getSelectionModel().getSelectedItem(),subject.getText(), messageBody.getText());
    }

    public void chooseFavoriteMail(){
        mailController.chooseFavoriteMail();
    }

    public void initComboboxFavoriteMail(List<String> allMail){
        mailReceiver.setItems(FXCollections.observableArrayList(allMail));
    }

    public void addMailToCombobox(String newMail) {
        mailReceiver.getItems().add(newMail);
        mailReceiver.setValue(newMail);}

}
