package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.connexion.LoginController;
import ulb.infof307.g01.controller.map.MapController;
import ulb.infof307.g01.controller.menu.HomePageMenuController;
import ulb.infof307.g01.controller.recipe.RecipeController;
import ulb.infof307.g01.controller.shoppingList.HomePageShoppingList;
import ulb.infof307.g01.view.HomePageViewController.*;
import ulb.infof307.g01.view.menu.HomeMenuViewController;


/**
 * Contrôleur principal de l'application.
 * Créé au démarrage de l'application.
 */
public class HomePageController extends Controller implements HomePageListener,ListenerBackPreviousWindow {

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

    @Override
    public void onShoppingListButtonClick() {
        HomePageShoppingList homePageShoppingList = new HomePageShoppingList(currentStage,this);
        homePageShoppingList.displayHomeShoppingList();
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

    @Override
    public void onReturn() {
        displayHome();
    }
}
