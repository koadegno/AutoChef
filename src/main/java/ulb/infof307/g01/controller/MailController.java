package ulb.infof307.g01.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.model.Mail;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.Window;
import ulb.infof307.g01.view.mail.FavoriteMailView;
import ulb.infof307.g01.view.mail.MailView;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MailController extends Window {
    private ShoppingList shoppingList;
    private MailView mailView;
    private FavoriteMailView favoriteMailView;
    private Stage popupStageMail;

    public MailController(ShoppingList shoppingList){
        this.shoppingList = shoppingList;
        createMailViewController();
    }

    private void createMailViewController() {
        this.mailView = new MailView();
        mailView.setMailController(this);
        try {
            this.popupStageMail = popupFXML("createMail.fxml", mailView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(String recipientAddress, String subject, String mailTextBody){
        setNodeColor(mailView.mailReceiver, false);
        Mail mail = new Mail();
        try {
            mail.sendMail(recipientAddress, shoppingList, subject, mailTextBody);
            popupStageMail.close();
        } catch (MessagingException e) {
            //TODO: colorier de cette maniere??
            setNodeColor(mailView.mailReceiver, true);
        }
    }

    public void chooseFavoriteMail(){
        this.favoriteMailView = new FavoriteMailView();
        favoriteMailView.setMailController(this);
        try {
            popupFXML("favoriteMail.fxml", favoriteMailView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initComboboxFavoriteMail(){
        try {
            List<String> allMail = Configuration.getCurrent().getMailAddressDao().getAllName();
            mailView.initComboboxFavoriteMail(allMail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initMailView(){
        mailView.setShoppingListToMail(shoppingList);
        initComboboxFavoriteMail();
    }

    public void confirmMail(String newMail){
        setNodeColor(favoriteMailView.newFavoriteMail, false);
        if(isValidEmailAddress(newMail)){
            mailView.addMailToCombobox(newMail);
            favoriteMailView.closePopup();
        }
        else{
            //TODO: mettre en rouge de cette façon??
            setNodeColor(favoriteMailView.newFavoriteMail, true);
        }
    }

    public void addFavoriteMail(String newMail) {
        setNodeColor(favoriteMailView.newFavoriteMail, false);
        if(isValidEmailAddress(newMail)){
            try {
                Configuration.getCurrent().getMailAddressDao().insert(newMail);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mailView.addMailToCombobox(newMail);
            favoriteMailView.closePopup(); //TODO: a revoir
        }
        else{
            //TODO: mettre en rouge de cette façon??
            setNodeColor(favoriteMailView.newFavoriteMail, true);
        }
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
}
