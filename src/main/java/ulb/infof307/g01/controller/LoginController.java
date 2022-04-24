package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.view.LoginViewController;

import java.util.Objects;

public class LoginController extends Controller implements LoginViewController.LoginListener{

    private LoginViewController loginViewController;
    public LoginController(Stage primaryStage){
        setStage(primaryStage);
    }

    public void display(){
        FXMLLoader loader = loadFXML("HomeLogin.fxml");
        loginViewController = loader.getController();
        loginViewController.setListener(this);
    }
    @Override
    public void onLoginButtonClick(String pseudo,String password) {
        //TODO intégrer db
        /**
        if(pseudo existe pas){
            loginViewController.showPseudoError(true);
            loginViewController.showPasswordError(false);
        }
        //voir si mdp correspond
        else if(pseudo existe mais mauvais mdp) {
            loginViewController.showPseudoError(false);
            loginViewController.showPasswordError(true);
        }
        else {
            HomePageController homePageController = new HomePageController(currentStage);
            homePageController.displayMain();
        }*/
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayMain();


    }

    @Override
    public void onRegisterButtonClick() {
        //TODO connecter interface register

    }

    @Override
    public void onQuitButtonClick() { currentStage.close(); }

}
