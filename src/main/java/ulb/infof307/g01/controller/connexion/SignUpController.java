package ulb.infof307.g01.controller.connexion;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Address;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.connexion.SignUpViewController;

import java.sql.SQLException;

/**
 * Classe qui contrôle la creation d'un utilisateur
 * Est créée par le LoginController
 */
public class SignUpController extends Controller implements SignUpViewController.SignUpListener {

    private SignUpViewController signUpViewController;

    public SignUpController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
    }
    public void displaySignUp(){
        FXMLLoader loader = loadFXML("SignUp.fxml");
        signUpViewController = loader.getController();
        signUpViewController.setListener(this);
    }
    public void displayHome(){
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayHome();
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
                newUser = Configuration.getCurrent().getUserDao().get(newUser.getPseudo());
                Configuration.getCurrent().setCurrentUser(newUser);
                displayHome();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SignUpViewController.showErrorSQL();
        }
    }

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
        listenerBackPreviousWindow.onReturn();
    }
}
