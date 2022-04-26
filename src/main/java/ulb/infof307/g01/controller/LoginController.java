package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.model.Address;
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

    public void displayHome(){
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayMain();
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
            displayHome();
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
    public void onSubmitButton(String pseudo, String lastName, String firstName, String password, String confirmPassword, String country, String city, String streetName, String houseNumber, boolean isProfessional) {
        resetErrors();
        int errorCounter = 0;
        int noError = 0;

        if (pseudo.equals("")){
            signUpViewController.pseudoTextFieldError(true);
            errorCounter++;
        }
        if (lastName.equals("")){
            signUpViewController.lastNameTextFieldError(true);
            errorCounter++;
        }
        if (firstName.equals("")){
            signUpViewController.firstNameTextFieldError(true);
            errorCounter++;
        }
        if (password.equals("")){
            signUpViewController.passwordTextFieldError(true);
            errorCounter++;
        }
        if (confirmPassword.equals("")){
            signUpViewController.confirmPseudoTextFieldError(true);
            errorCounter++;
        }
        if (country.equals("")){
            signUpViewController.countryTextFieldError(true);
            errorCounter++;
        }
        if (city.equals("")){
            signUpViewController.cityTextFieldError(true);
            errorCounter++;
        }
        if (streetName.equals("")){
            signUpViewController.streetNameTextFieldError(true);
            errorCounter++;
        }
        if (houseNumber.equals("")){
            signUpViewController.houseNumberTextFieldError(true);
            errorCounter++;
        }

        if (errorCounter == noError){
            verifySignUpValues(pseudo, lastName, firstName, password, confirmPassword, country, city, streetName, Integer.parseInt(houseNumber), isProfessional);
        }
    }

    private void verifySignUpValues(String pseudo, String lastName, String firstName, String password, String confirmPassword, String country, String city, String streetName, int houseNumber, boolean isPro) {
        try {
            User existentUser = Configuration.getCurrent().getUserDao().get(pseudo);
            if (existentUser != null){
                signUpViewController.pseudoTextFieldError(true);
            }
            else if(!password.equals(confirmPassword)){
                signUpViewController.passwordTextFieldError(true);
                signUpViewController.confirmPseudoTextFieldError(true);
            }
            else{
                int postalCode = 1700;
                int noID = -1;
                Address newUserAdress = new Address(country, city, postalCode, streetName, houseNumber);
                User newUser = new User(noID, lastName, firstName, pseudo, password, newUserAdress, isPro);
                Configuration.getCurrent().getUserDao().insert(newUser);
                Configuration.getCurrent().setCurrentUser(newUser);
                displayHome();
            }
        } catch (SQLException e) {
           SignUpViewController.showErrorSQL();
        }
    }

    public void resetErrors(){
        signUpViewController.pseudoTextFieldError(false);
        signUpViewController.lastNameTextFieldError(false);
        signUpViewController.firstNameTextFieldError(false);
        signUpViewController.passwordTextFieldError(false);
        signUpViewController.confirmPseudoTextFieldError(false);
        signUpViewController.countryTextFieldError(false);
        signUpViewController.cityTextFieldError(false);
        signUpViewController.streetNameTextFieldError(false);
        signUpViewController.houseNumberTextFieldError(false);
    }

    @Override
    public void onCancelButton() {
        displayHomeLogin();
    }
}
