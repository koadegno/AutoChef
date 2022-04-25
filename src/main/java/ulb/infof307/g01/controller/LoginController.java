package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.LoginViewController;
import ulb.infof307.g01.view.SignUpViewController;

import java.sql.SQLException;
import java.util.Objects;

public class LoginController extends Controller implements LoginViewController.LoginListener, SignUpViewController.SignUpListener {

    private LoginViewController loginViewController;
    private SignUpViewController signUpViewController;
    public LoginController(Stage primaryStage){
        setStage(primaryStage);
    }

    /**
    * Affiche la page du login
     * @see ulb.infof307.g01.Main
     */
    public void displayHomeLogin(){
        FXMLLoader loader = loadFXML("HomeLogin.fxml");
        loginViewController = loader.getController();
        loginViewController.setListener(this);
    }

    public void displaySignUp(){
        FXMLLoader loader = loadFXML("SignUp.fxml");
        signUpViewController = loader.getController();
        signUpViewController.setListener(this);
    }

    /**
     * Verifie si le login peut avoir lieu, si oui redirection vers la homePage.
     */
    @Override
    public void onLoginButtonClick(String pseudo,String password) {
        User user;
        try {
            user = Configuration.getCurrent().getUserDao().get(pseudo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) { // pas de user trouvé donc le pseudo n'est pas bon
            loginViewController.showPseudoError(true);
            loginViewController.showPasswordError(false);
        }
        //voir si mdp correspond
        else if (!Objects.equals(user.getPassword(), password)){// pseudo trouvé mais pas le bon mdp
            loginViewController.showPseudoError(false);
            loginViewController.showPasswordError(true);
        }
        else{ //bon mdp et bon pseudo
            Configuration.getCurrent().setCurrentUser(user);
            HomePageController homePageController = new HomePageController(currentStage);
            homePageController.displayMain();
        }

    }

    @Override
    public void onSignUpButtonClick() {
        displaySignUp();
    }

    @Override
    public void onQuitButtonClick() { currentStage.close(); }

    // SIGN UP

    @Override
    public void onSubmitButton(String pseudo, String lastName, String firstName, String password, String confirmPassword, String country, String city, String streetName, String houseNumber) {

    }

    @Override
    public void onCancelButton() {
        displayHomeLogin();
    }
}
