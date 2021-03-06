package ulb.infof307.g01.model.export;

import javafx.scene.control.Alert;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.exception.DocumentException;
import ulb.infof307.g01.view.ViewController;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Classe permettant d'envoyer la liste de course en PDF par Mail, utilise la librairie {@link javax.mail}.
 */
public class Mail {
    private final static String DEFAULT_ADDRESS = "autochef@outlook.fr";
    private final static String DEFAULT_ADDRESS_PASSWORD = "43fZ5LD4gEJk74B";

    private final static String SMTP_SERVER = "smtp-mail.outlook.com";
    private final static int SMTP_PORT = 587;
    private final static boolean TLS_ENABLED = true;

    private Session session;


    public Mail() {
        createSession();
    }

    /**
     * Envoie une @{code ShoppingList} par mail.
     * @param recipientAddress Adresse e-mail du destinataire du mail.
     * @param shoppingListToSend Liste de Course à envoyé (en pièce-jointe).
     * @param subject l'objet du mail
     * @param mailTextBody texte qui contient la description du mail
     * @throws AddressException Si l'adresse e-mail n'est pas dans un format valide.
     * @throws MessagingException Pour toutes les autres erreurs.
     */
    public void sendMail(String recipientAddress, ShoppingList shoppingListToSend, String subject, String mailTextBody) throws MessagingException {
        createSession();
        Message mail = composeMessage(recipientAddress, shoppingListToSend, subject, mailTextBody);
        Transport.send(mail);
    }

    /**
     * Initialise la Session (Host SMTP, Port du serveur SMTP, Authentification, Chiffrement,...).
     */
    private void createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", String.valueOf(TLS_ENABLED));
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(DEFAULT_ADDRESS, DEFAULT_ADDRESS_PASSWORD);
            }
        });
    }

    /**
     * Créer un {@link Message} contenant la {@code ShoppingList} en pièce-jointe.
     * @param recipientAddress Adresse e-mail du destinataire du mail.
     * @param shoppingListToSend Liste de Course à envoyé (en pièce-jointe).
     * @return le {@code Message} créé.
     * @throws AddressException Si l'adresse e-mail n'est pas dans un format valide.
     * @throws MessagingException Pour toutes les autres erreurs.
     */
    private Message composeMessage(String recipientAddress, ShoppingList shoppingListToSend, String subject, String mailTextBody) throws AddressException, MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(DEFAULT_ADDRESS));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));

        message.setSubject(subject);

        Multipart messageBody = new MimeMultipart();

        messageBody.addBodyPart(addText(mailTextBody));

        DocumentCreator pdfCreator = new PDFCreator();
        try {
            pdfCreator.createDocument(shoppingListToSend);
        } catch (DocumentException e) {
            ViewController.showAlert(Alert.AlertType.ERROR, "ERREUR", "Une erreur s'est produite lors de l'exportation en PDF.");
        }

        messageBody.addBodyPart(addAttachment(System.getProperty("user.dir") + "\\" + shoppingListToSend.getName() + ".pdf"));

        message.setContent(messageBody);

        return message;
    }

    /**
     * Créer une {@link BodyPart} contenant le texte du mail.
     * @param mailTextBody texte qui contient la description du mail
     * @return la {@code BodyPart} créée.
     * @throws MessagingException si une exception est déclenchée par la librairie.
     */
    private BodyPart addText(String mailTextBody) throws MessagingException {
        BodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(mailTextBody);
        return textBodyPart;
    }

    /**
     * Créer une {@link BodyPart} contenant une pièce-jointe.
     * @param attachmentFilePath Le chemin vers le fichier à joindre au Mail.
     * @return la {@code BodyPart} créée.
     * @throws MessagingException si une exception est déclenchée par la librairie.
     */
    private BodyPart addAttachment(String attachmentFilePath) throws MessagingException {

        BodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachmentFilePath);

        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName("Liste_De_Course.pdf");

        return attachmentBodyPart;
    }
}
