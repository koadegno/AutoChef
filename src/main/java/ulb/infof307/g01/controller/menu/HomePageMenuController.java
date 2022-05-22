package ulb.infof307.g01.controller.menu;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.view.HomePageViewController;
import ulb.infof307.g01.view.menu.HomeMenuViewController;

/**
 * Classe qui contrôle le Home du menu
 * Permet d'accéder à la création d'un menu et l'affichage de tous les menus créés par l'utilisateur
 */
public class HomePageMenuController extends Controller implements HomeMenuViewController.HomeMenuListener, ListenerBackPreviousWindow {

    public HomePageMenuController(ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
    }

    public void displayHomeMenu(){
        FXMLLoader loader = loadFXML("HomeMenu.fxml");
        ulb.infof307.g01.view.menu.HomeMenuViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    /**
     * Revient à la page d'accueil de l'application.
     * @see HomePageViewController
     * */
    @Override
    public void onBackButtonClick() {
        listenerBackPreviousWindow.onReturn();
    }

    /**
     * Affiche la page permettant à l'utilisateur de consulter sa liste de Menus
     * @see ulb.infof307.g01.model.Menu
     * //TODO: @see ControllerMenu + ViewController Liste Menu
     */
    @Override
    public void onUserMenusButtonClick() {
        UserMenusController userMenusController = new UserMenusController(this);
        userMenusController.displayAllMenus();
    }

    /**
     * Affiche la page permettant à l'utilisateur de créer un nouveau Menu
     * @see ulb.infof307.g01.model.Menu
     * //TODO: @see ControllerMenu + ViewController CreateMenu
     */
    @Override
    public void onUserCreateMenuButtonClick() {
        CreateMenuController createMenuController = new CreateMenuController(currentStage,this);
        createMenuController.displayCreateMenu();
    }

    @Override
    public void logout() { userLogout();}

    @Override
    public void onReturn() { displayHomeMenu(); }
}
