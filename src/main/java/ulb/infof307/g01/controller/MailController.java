package ulb.infof307.g01.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.model.Mail;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.mail.FavoriteMailView;
import ulb.infof307.g01.view.mail.MailView;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MailController extends Controller implements MailView.Listener, FavoriteMailView.Listener  {
    private ShoppingList shoppingList;
    private MailView mailView;
    private FavoriteMailView favoriteMailView;
    private Stage popupStageMail, popupFavoriteMail;

    public MailController(ShoppingList shoppingList){
        this.shoppingList = shoppingList;
        createMailViewController();
    }

    private void createMailViewController() {
        this.mailView = new MailView();
        mailView.setListener(this);
        try {
            this.popupStageMail = popupFXML("createMail.fxml", mailView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMail(String recipientAddress, String subject, String mailTextBody){
        mailView.showAddressMailError(false);
        Mail mail = new Mail();
        try {
            if(!Objects.equals(recipientAddress, null)){
                mail.sendMail(recipientAddress, shoppingList, subject, mailTextBody);
                popupStageMail.close();
            }
            else{
                mailView.showAddressMailError(true);
            }
        } catch (MessagingException e) {
            mailView.showAddressMailError(true);
        }
    }

    @Override
    public void chooseFavoriteMail(){
        this.favoriteMailView = new FavoriteMailView();
        favoriteMailView.setListener(this);
        try {
             this.popupFavoriteMail = popupFXML("favoriteMail.fxml", favoriteMailView);
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

    @Override
    public void saveFavoriteMail(String newMail, boolean isSave){
        favoriteMailView.showAddressMailError(false);
        if(isValidEmailAddress(newMail)){
            try {
                if(isSave){ //Enregistre le mail favorie dans la bdd
                    Configuration.getCurrent().getMailAddressDao().insert(newMail);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mailView.addMailToCombobox(newMail);
            popupFavoriteMail.close();
        }
        else{
            favoriteMailView.showAddressMailError(true);
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
