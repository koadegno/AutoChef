package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Map.MapController;
import ulb.infof307.g01.controller.menu.MenuController;
import ulb.infof307.g01.controller.menu.UserMenusController;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.view.*;
import ulb.infof307.g01.view.HomePageViewController.*;
import ulb.infof307.g01.view.menu.HomeMenuViewController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.HomeShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewController;


/**
 * Contrôleur principal de l'application.
 * Créé au démarrage de l'application.
 */
public class HomePageController extends Controller implements HomePageListener, HomeShoppingListViewController.Listener, HomeMenuViewController.HomeMenuListener {

    // Méthodes de la fenêtre d'accueil

    public HomePageController(Stage primaryStage){
        setStage(primaryStage);
    }


    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * */
    public void displayMain() {
        FXMLLoader loader = loadFXML("HomePage.fxml");
        ulb.infof307.g01.view.HomePageViewController viewController = loader.getController();
        viewController.setListener(this);

    }

    /**
     * Affiche la page principale des Listes de Courses.
     * @see HomeShoppingListViewController
     * */
    @Override
    public void onShoppingListButtonClick() {
        HomeShoppingListViewController homeShoppingListViewController = new HomeShoppingListViewController();
        homeShoppingListViewController.setListener(this);
        loadFXML(homeShoppingListViewController, "HomeShoppingList.fxml");

    }

    /**
     * Affiche la page principale des Menus.
     * @see HomeMenuViewController
     * */
    @Override
    public void onMenuButtonClick() {
        FXMLLoader loader = this.loadFXML("HomeMenu.fxml");
        HomeMenuViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Carte
     */
    @Override
    public void onMapButtonClick() {
        MapController mapController = new MapController(currentStage);
        mapController.show();
    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Accueil Recette
     */
    @Override
    public void onRecipeButtonClick() {
        RecipeController controller = new RecipeController();
        controller.displayMain();
    }

    /**
     * Ferme la fenêtre et termine le programme.
     */
    @Override
    public void onQuitButtonClick() {
        currentStage.close();
    }

    // Méthodes de la page principale de ShoppingList

    /**
     * Affiche la page qui liste les ShoppingList
     * @see ulb.infof307.g01.model.ShoppingList
     * //TODO: @see Contrôlleur de vue liste ShoppingList
     */
    @Override
    public void onMyShoppingListsButtonClick() {
        UserShoppingListViewController windowUserShoppingListsController = new UserShoppingListViewController();
        loadFXML(windowUserShoppingListsController, "CreateUserShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(windowUserShoppingListsController, this);

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(false);

    }

    /**
     * Affiche la page de création de Liste de courses
     * @see ulb.infof307.g01.model.ShoppingList
     * //TODO: @see Contrôlleur de vue ShoppingList principal
     */
    @Override
    public void onCreateShoppingListsButtonClick() {
        CreateUserShoppingListViewController createUserShoppingListViewController = new CreateUserShoppingListViewController();
        loadFXML(createUserShoppingListViewController, "CreateUserShoppingList.fxml");
        ShoppingListController shoppingListController = new ShoppingListController(createUserShoppingListViewController, this );

        //Initialise la page avec les informations de la bdd
        shoppingListController.initInformationShoppingList(true);

    }

    /**
     * Revient à la page d'accueil de l'application.
     * @see HomePageViewController
     * */
    @Override
    public void onBackButtonClick() {
        FXMLLoader loader = this.loadFXML("HomePage.fxml");
        HomePageViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    // Méthodes de la fenêtre principale des Menus

    /**
     * Affiche la page permettant à l'utilisateur de consulter sa liste de Menus
     * @see ulb.infof307.g01.model.Menu
     * //TODO: @see ControllerMenu + ViewController Liste Menu
     */
    @Override
    public void onUserMenusButtonClick() {
        UserMenusController userMenusController = new UserMenusController(currentStage);
        userMenusController.showAllMenus();
    }

    /**
     * Affiche la page permettant à l'utilisateur de créer un nouveau Menu
     * @see ulb.infof307.g01.model.Menu
     * //TODO: @see ControllerMenu + ViewController CreateMenu
     */
    @Override
    public void onUserCreateMenuButtonClick() {
        MenuController menuController = new MenuController(currentStage);
        menuController.showCreateMenu();
    }
}
