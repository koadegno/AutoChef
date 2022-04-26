package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpViewController extends ViewController<SignUpViewController.SignUpListener> implements Initializable {

    @FXML
    private TextField pseudoTextField,
            lastNameTextField, firstNameTextField,
            passwordTextField, confirmPasswordTextField,
            countryTextField, cityTextField,
            streetNameTextField, houseNumberTextField;

    @FXML
    private CheckBox isProfessionalCheckBox;

    public void onSubmitButton() {
        String pseudo = pseudoTextField.getText();
        String lastName = lastNameTextField.getText();
        String firstName = firstNameTextField.getText();
        String password =  passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        String country = countryTextField.getText();
        String city =  cityTextField.getText();
        String streetName = streetNameTextField.getText();
        String houseNumber =  houseNumberTextField.getText();
        boolean isProfessional = isProfessionalCheckBox.isSelected();

        listener.onSubmitButton(pseudo, lastName, firstName, password, confirmPassword, country, city, streetName, houseNumber, isProfessional);
    }

    public void pseudoTextFieldError(boolean isError) { setNodeColor(pseudoTextField, isError); }

    public void lastNameTextFieldError(boolean isError) {
        setNodeColor(lastNameTextField, isError);
    }

    public void firstNameTextFieldError(boolean isError) { setNodeColor(firstNameTextField, isError); }

    public void passwordTextFieldError(boolean isError) {
        setNodeColor(passwordTextField, isError);
    }

    public void confirmPseudoTextFieldError(boolean isError) {
        setNodeColor(confirmPasswordTextField, isError);
    }

    public void countryTextFieldError(boolean isError) {
        setNodeColor(countryTextField, isError);
    }

    public void cityTextFieldError(boolean isError) {
        setNodeColor(cityTextField, isError);
    }

    public void streetNameTextFieldError(boolean isError) {
        setNodeColor(streetNameTextField, isError);
    }

    public void houseNumberTextFieldError(boolean isError) {
        setNodeColor(houseNumberTextField, isError);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.onlyIntValue(houseNumberTextField);
    }

    public void onCancelButton() {
        listener.onCancelButton();
    }

    public interface SignUpListener {
        void onSubmitButton(String pseudo, String lastName, String firstName, String password, String confirmPassword, String country, String city, String streetName, String houseNumber, boolean isProfessional);
        void onCancelButton();
    }

}
