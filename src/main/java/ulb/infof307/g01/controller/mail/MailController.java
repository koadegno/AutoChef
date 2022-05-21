package ulb.infof307.g01.controller.mail;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.database.dao.MailAddressDao;
import ulb.infof307.g01.model.exception.DuplicatedKeyException;
import ulb.infof307.g01.model.export.Mail;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.mail.FavoriteMailViewController;
import ulb.infof307.g01.view.mail.MailViewController;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Classe qui contrôle les mails
 * permet d'envoyer ses listes de courses par mails
 */
public class MailController extends Controller implements MailViewController.Listener, FavoriteMailViewController.Listener  {
    private final ShoppingList shoppingList;
    private MailViewController mailViewController;
    private FavoriteMailViewController favoriteMailViewController;
    private Stage popupStageMail, popupFavoriteMail;

    public MailController(ShoppingList shoppingList){
        this.shoppingList = shoppingList;
        createMailViewController();
        initMailView();
    }

    /**
     * Affiche la popup qui permet d'envoyer un mail
     */
    private void createMailViewController() {
        this.mailViewController = new MailViewController();
        mailViewController.setListener(this);
        try {
            this.popupStageMail = popupFXML("CreateMail.fxml", mailViewController);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing("CreateMail.fxml");
        }
    }

    /**
     * Permet d'envoyer une liste de course par mail
     * @param recipientAddress Adresse e-mail du destinataire du mail.
     * @param subject l'objet du mail
     * @param mailTextBody texte qui contient la description du mail
     */
    @Override
    public void sendMail(String recipientAddress, String subject, String mailTextBody){
        mailViewController.showAddressMailError(false);
        Mail mail = new Mail();
        try {
            if(!Objects.equals(recipientAddress, null)){
                mail.sendMail(recipientAddress, shoppingList, subject, mailTextBody);
                popupStageMail.close(); //Fermer la popup
            }
            else{
                mailViewController.showAddressMailError(true); //l'utilisateur n'a pas choisi de mail
            }
        } catch (MessagingException e) {
            mailViewController.showAddressMailError(true);
        }
    }

    /**
     * Affiche la popup qui permet de créer un nouveau mail (favorite)
     */
    @Override
    public void createFavoriteMail(){
        this.favoriteMailViewController = new FavoriteMailViewController();
        favoriteMailViewController.setListener(this);
        String filename = "FavoriteMail.fxml";
        try {
            this.popupFavoriteMail = popupFXML(filename, favoriteMailViewController);
        } catch (IOException e) {
            ViewController.showErrorFXMLMissing(filename);
        }
    }

    @Override
    public void logout() {
        userLogout();
        popupStageMail.close();
    }

    @Override
    public void helpCreateMailClicked() {
        int numberOfImageHelp = 7;
        String directory = "helpMail/";
        HelpController helpController = new HelpController(directory, numberOfImageHelp);
        helpController.displayHelpShop();
    }

    private void initComboboxFavoriteMail(){
        try {
            MailAddressDao mailAddressDao = configuration.getMailAddressDao();
            List<String> allMail = mailAddressDao.getAllName();
            mailViewController.initComboboxFavoriteMail(allMail);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    private void initMailView(){
        mailViewController.showNameShoppingListToMail(shoppingList);
        initComboboxFavoriteMail();
    }

    /**
     * Enregistre le mail favorite dans la base de donnée
     * @param newMail nouvelle adresse e-mail du destinataire du mail.
     * @param isSave boolean permet de savoir si l'utilisateur veut enregistrer le mail dans la bdd
     */
    @Override
    public void saveFavoriteMail(String newMail, boolean isSave){
        favoriteMailViewController.showAddressMailError(false);
        if(isValidEmailAddress(newMail)){
            try {
                if(isSave){ //Enregistre le mail favorite dans la bdd
                    MailAddressDao mailAddressDao = configuration.getMailAddressDao();
                    mailAddressDao.insert(newMail);
                    this.initComboboxFavoriteMail();
                }
                else{
                    List<String> mails = mailViewController.getMailInComboBox();
                    if(!mails.contains(newMail)){
                        mailViewController.addMailToCombobox(newMail);
                    }
                }
            } catch (SQLException | DuplicatedKeyException e) {
                MailViewController.showAlert(Alert.AlertType.ERROR, "Erreur", "Le mail enregistrée existe déjà dans vos favoris");
            }

            popupFavoriteMail.close(); //Ferme la popup
        }
        else{
            favoriteMailViewController.showAddressMailError(true); //l'utilisateur a mal écrit le mail
        }
    }


    /**
     * Permet de savoir si l'adresse e-mail entrée est correcte
     * @param email adresse e-mail du destinataire du mail.
     * @return boolean qui indique si c'est une adresse e-mail valide
     */
    private boolean isValidEmailAddress(String email) {
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
