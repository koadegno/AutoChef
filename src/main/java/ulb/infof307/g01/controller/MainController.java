package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.shoppingList.ShoppingListController;
import ulb.infof307.g01.view.WindowHomeController;
import ulb.infof307.g01.view.*;
import ulb.infof307.g01.view.HomePageController.*;
import ulb.infof307.g01.view.menu.HomeMenuController;
import ulb.infof307.g01.view.menu.WindowCreateMenuController;
import ulb.infof307.g01.view.menu.WindowUserMenuListController;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.HomeShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewViewController;

import java.sql.SQLException;

/**
 * Contrôleur principal de l'application.
 * Créé au démarrage de l'application.
 */
public class MainController extends Controller implements HomePageListener, HomeShoppingListViewController.Listener, HomeMenuController.HomeMenuListener {

    // Méthodes de la fenêtre d'accueil

    /**
     * Affiche la page principale de l'application.
     * @see ulb.infof307.g01.Main
     * */
    public void displayMain(Stage mainStage) {
        FXMLLoader loader = this.loadFXML("HomePage.fxml");
        HomePageController viewController = loader.getController();
        viewController.setListener(this);

        setStage(mainStage);
    }

    /**
     * Affiche la page principale des Listes de Courses.
     * @see HomeShoppingListViewController
     * */
    @Override
    public void onShoppingListButtonClick() {
        //TODO: reprendre le code de nathan plus tard
        HomeShoppingListViewController homeShoppingListViewController = new HomeShoppingListViewController();
        homeShoppingListViewController.setListener(this);
        loadFXML(homeShoppingListViewController, "HomeShoppingList.fxml");

    }

    /**
     * Affiche la page principale des Menus.
     * @see ulb.infof307.g01.view.menu.HomeMenuController
     * */
    @Override
    public void onMenuButtonClick() {
        FXMLLoader loader = this.loadFXML("HomeMenu.fxml");
        HomeMenuController viewController = loader.getController();
        viewController.setListener(this);

        setNewScene(loader);
    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Carte
     */
    @Override
    public void onMapButtonClick() {

    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Accueil Recette
     */
    @Override
    public void onRecipeButtonClick() {

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
        UserShoppingListViewViewController windowUserShoppingListsController = new UserShoppingListViewViewController();
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
     * @see WindowHomeController
     * */
    @Override
    public void onBackButtonClick() {
        //TODO: controller n'est pas de type HomePageController????
        this.loadFXML("HomePage.fxml");
    }

    // Méthodes de la fenêtre principale des Menus

    /**
     * Affiche la page permettant à l'utilisateur de consulter sa liste de Menus
     * @see ulb.infof307.g01.model.Menu
     * //TODO: @see ControllerMenu + ViewController Liste Menu
     */
    @Override
    public void onUserMenusButtonClick() {
        // TODO: REFACTOR MVC
        WindowUserMenuListController menusController = new WindowUserMenuListController();
        menusController.displayMyMenus();
    }

    /**
     * Affiche la page permettant à l'utilisateur de créer un nouveau Menu
     * @see ulb.infof307.g01.model.Menu
     * //TODO: @see ControllerMenu + ViewController CreateMenu
     */
    @Override
    public void onUserCreateMenuButtonClick() {
        // TODO: REFACTOR MVC
        WindowCreateMenuController createMenu = null;
        try {
            createMenu = new WindowCreateMenuController();
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
        createMenu.displayEditMeal();
    }
}
