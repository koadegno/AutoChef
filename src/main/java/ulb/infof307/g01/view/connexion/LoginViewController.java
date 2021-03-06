package ulb.infof307.g01.view.connexion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ulb.infof307.g01.view.ViewController;

/**
 * La classe gère la partie vue pour la connexion d'un compte
 */

public class LoginViewController extends ViewController<LoginViewController.Listener> {
    @FXML
    private TextField password;

    @FXML
    private TextField pseudo;

    @FXML
    void closeApplication(ActionEvent event) {listener.onQuitButtonClick();}

    @FXML
    void login(ActionEvent event) {listener.onLoginButtonClick(pseudo.getText(),password.getText());}

    @FXML
    void signUp(ActionEvent event) {listener.onSignUpButtonClick();}

    public void showPseudoError(boolean isError) {setNodeColor(pseudo,isError);}

    public void showPasswordError(boolean isError) {setNodeColor(password,isError);}

    public interface Listener {
        void onLoginButtonClick(String pseudo,String password);
        void onSignUpButtonClick();
        void onQuitButtonClick();
    }
}
