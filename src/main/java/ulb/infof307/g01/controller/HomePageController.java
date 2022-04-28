package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.connexion.LoginController;
import ulb.infof307.g01.controller.map.MapController;
import ulb.infof307.g01.controller.menu.HomePageMenuController;
import ulb.infof307.g01.controller.menu.MenuController;
import ulb.infof307.g01.controller.menu.UserMenusController;
import ulb.infof307.g01.controller.recipe.RecipeController;
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
public class HomePageController extends Controller implements HomePageListener, HomeShoppingListViewController.Listener,ListenerBackPreviousWindow {

    // Méthodes de la fenêtre d'accueil

    public HomePageController(Stage primaryStage){
        setStage(primaryStage);
    }


    /**
     * Affiche la page principale de l'application.
     * @see LoginController
     * */
    public void displayHome() {
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
        HomePageMenuController homePageMenuController = new HomePageMenuController(currentStage,this);
        homePageMenuController.displayHomeMenu();
    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Carte
     */
    @Override
    public void onMapButtonClick() {
        MapController mapController = new MapController(currentStage,this);
        mapController.displayMap();
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
    public void logout() {
        userLogout();
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
        loadFXML(windowUserShoppingListsController, "UserShoppingList.fxml");
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
        loadFXML(createUserShoppingListViewController, "UserShoppingList.fxml");
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


    @Override
    public void onReturn() {
        displayHome();
    }
}
