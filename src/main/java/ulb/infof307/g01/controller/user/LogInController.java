package ulb.infof307.g01.controller.user;

import javafx.fxml.FXMLLoader;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.view.user.SignUpViewController;

public class LogInController extends Controller implements SignUpViewController.SignUpListener {

    public void displayMain(){
        FXMLLoader loader = loadFXML("SignUp.fxml");
        SignUpViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    @Override
    public void onSubmitButton(String pseudo, String lastName, String firstName, String password, String confirmPassword, String country, String city, String streetName, String houseNumber) {

    }

    @Override
    public void onCancelButton() {

    }
}
