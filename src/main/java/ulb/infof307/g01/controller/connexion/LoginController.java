package ulb.infof307.g01.controller.connexion;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.User;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.UserDao;
import ulb.infof307.g01.view.connexion.LoginViewController;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Classe qui contrôle la gestion du login
 * Créée au démarrage
 */
public class LoginController extends Controller implements LoginViewController.LoginListener,  ListenerBackPreviousWindow {

    private LoginViewController loginViewController;


    public LoginController(Stage primaryStage){
        setStage(primaryStage);
    }

    /**
    * Affiche la page du connexion
     * @see ulb.infof307.g01.Main
     */
    public void displayHomeLogin(){
        FXMLLoader loader = loadFXML("HomeLogin.fxml");
        loginViewController = loader.getController();
        loginViewController.setListener(this);
    }


    public void displayHome(){
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayHome();
    }

    /**
     * Verifie si le connexion peut avoir lieu, si oui redirection vers la homePage.
     */
    @Override
    public void onLoginButtonClick(String pseudo,String password) {
        User user;

        try {
            UserDao userDao = configuration.getUserDao();
            user = userDao.get(pseudo);
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
            configuration.setCurrentUser(user);
            displayHome();
        }


    }

    @Override
    public void onSignUpButtonClick() {
        SignUpController signUpController = new SignUpController(currentStage,this);
        signUpController.displaySignUp();
    }

    @Override
    public void onQuitButtonClick() { currentStage.close(); }

    // SIGN UP

    @Override
    public void onReturn() {this.displayHomeLogin();}
}
