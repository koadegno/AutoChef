package ulb.infof307.g01.view.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpViewController extends ViewController<SignUpViewController.SignUpListener> implements Initializable {

    @FXML
    private TextField pseudoTextField, lastNameTextField, firstNameTextField, passwordTextField, confirmPasswordTextField, countryTextField, cityTextField, streetNameTextField, houseNumberTextField;

    public void verifyTextFieldsInformations() {
        String pseudo = pseudoTextField.getText();
        String lastName = lastNameTextField.getText();
        String firstName = firstNameTextField.getText();
        String password =  passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        String country = countryTextField.getText();
        String city =  cityTextField.getText();
        String streetName = streetNameTextField.getText();
        String houseNumber =  houseNumberTextField.getText();

        listener.onSubmitButton(pseudo, lastName, firstName, password, confirmPassword, country, city, streetName, houseNumber);
    }

    public void pseudoTextFieldError() {
        setNodeColor(pseudoTextField, true);
    }

    public void lastNameTextFieldError() {
        setNodeColor(lastNameTextField, true);
    }

    public void firstNameTextFieldError() {
        setNodeColor(firstNameTextField, true);
    }

    public void passwordTextFieldError() {
        setNodeColor(passwordTextField, true);
    }

    public void confirmPseudoTextFieldError() {
        setNodeColor(confirmPasswordTextField, true);
    }

    public void countryTextFieldError() {
        setNodeColor(countryTextField, true);
    }

    public void cityTextFieldError() {
        setNodeColor(cityTextField, true);
    }

    public void streetNameTextFieldError() {
        setNodeColor(streetNameTextField, true);
    }

    public void houseNumberTextFieldError() {
        setNodeColor(houseNumberTextField, true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.onlyIntValue(houseNumberTextField);
    }

    public void onCancelButton() {
        listener.onCancelButton();
    }

    public interface SignUpListener {
        void onSubmitButton(String pseudo, String lastName, String firstName, String password, String confirmPassword, String country, String city, String streetName, String houseNumber);
        void onCancelButton();
    }

}
