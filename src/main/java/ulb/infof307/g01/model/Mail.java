package ulb.infof307.g01.model;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Mail {
    private final static String defaultAddress = "autochef@outlook.fr";
    private final static String defaultAddressPassword = "43fZ5LD4gEJk74B";

    private final static String SMTP_SERVER = "smtp-mail.outlook.com";
    private final static int SMTP_PORT = 587;
    private final static boolean TLS_ENABLED = true;

    private Session session;

    public Mail() {
        createSession();
    }

    public void sendMail(String recipientAddress) throws MessagingException {
        createSession();
        Message mail = composeMessage(recipientAddress);
        Transport.send(mail);
    }

    private void createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", String.valueOf(TLS_ENABLED));
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(defaultAddress, defaultAddressPassword);
            }
        });
    }

    private Message composeMessage(String recipientAddress) throws AddressException, MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(defaultAddress));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));

        message.setSubject("Liste de courses"); // TODO : nom Liste de courses

        Multipart messageBody = new MimeMultipart();

        messageBody.addBodyPart(addText());
        messageBody.addBodyPart(addAttachment("S:\\Nathan\\Pictures\\E0Nj0xaWQAIM43b.jpg"));

        message.setContent(messageBody);

        return message;
    }

    private BodyPart addText() throws MessagingException {
        BodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText("On vous as envoyé cette Liste de Course en pièce jointe");  // TODO : nom Liste de courses
        return textBodyPart;
    }
    private BodyPart addAttachment(String attachmentFilePath) throws MessagingException {

        BodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachmentFilePath);

        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName("Liste_De_Course.jpg"); // TODO : gérer odt

        return attachmentBodyPart;
    }
}
