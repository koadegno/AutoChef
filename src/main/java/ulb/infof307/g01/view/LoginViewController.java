package ulb.infof307.g01.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginViewController extends ViewController<LoginViewController.LoginListener>{
    @FXML
    private TextField password;

    @FXML
    private TextField pseudo;

    @FXML
    void closeApplication(ActionEvent event) {listener.onQuitButtonClick();}

    @FXML
    void login(ActionEvent event) {listener.onLoginButtonClick(pseudo.getText(),password.getText());}

    @FXML
    void register(ActionEvent event) {listener.onRegisterButtonClick();}

    public void showPseudoError(boolean isError) {setNodeColor(pseudo,isError);}

    public void showPasswordError(boolean isError) {setNodeColor(password,isError);}

    public interface LoginListener {
        void onLoginButtonClick(String pseudo,String password);
        void onRegisterButtonClick();
        void onQuitButtonClick();
    }
}
