package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.connexion.LoginController;
import ulb.infof307.g01.controller.map.MapController;
import ulb.infof307.g01.controller.menu.HomePageMenuController;
import ulb.infof307.g01.controller.recipe.HomePageRecipeController;
import ulb.infof307.g01.controller.shop.HomeShopController;
import ulb.infof307.g01.controller.shoppingList.HomePageShoppingListController;
import ulb.infof307.g01.view.HomePageViewController.*;
import ulb.infof307.g01.view.menu.HomeMenuViewController;


/**
 * Contrôleur principal de l'application.
 * Permet d'aller sur les 4 fonctionnalités principales : liste de courses, menus, recettes et la map
 * Créé quand l'utilisateur se connecte
 */
public class HomePageController extends Controller implements HomePageListener,ListenerBackPreviousWindow {

    // Méthodes de la fenêtre d'accueil
    public HomePageController(){}

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
        HomePageShoppingListController homePageShoppingList = new HomePageShoppingListController(currentStage,this);
        homePageShoppingList.displayHomeShoppingList();
    }

    /**
     * Affiche la page principale des Menus.
     * @see HomeMenuViewController
     * */
    @Override
    public void onMenuButtonClick() {
        HomePageMenuController homePageMenuController = new HomePageMenuController(this);
        homePageMenuController.displayHomeMenu();
    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Carte
     */
    @Override
    public void onMapButtonClick() {
        Boolean notReadOnlyMode = false;
        MapController mapController = new MapController(this,notReadOnlyMode );
        mapController.displayMap();
    }

    /**
     * Affiche la page principale de la carte
     * //TODO: @see ControllerDeVue Accueil Recette
     */
    @Override
    public void onRecipeButtonClick() {
        HomePageRecipeController controller = new HomePageRecipeController();
        controller.displayHomeRecipe();
    }

    /**
     * Affiche la page principale des magasins
     * //TODO: @see ControllerDeVue Accueil Magasins
     */
    @Override
    public void onShopButtonClick() {
        HomeShopController controller = new HomeShopController(currentStage,this);
        controller.displayHomeShop();
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
