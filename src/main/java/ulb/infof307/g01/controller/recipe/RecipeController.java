package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.recipe.*;

import java.util.List;


/**
 * Contrôleur responsable de tous les écrans en lien avec les recettes
 */
public class RecipeController extends Controller implements HomeRecipeViewController.HomeRecipeListener, ListenerBackPreviousWindow {

    // private Controller parentController; //TODO

    Scene sceneViewRecipe = null;

    Scene sceneFavoriteRecipe = null;

    boolean isWaitingModification = false;

    private CreateRecipeViewController createRecipeViewController;
    private UserRecipesViewController userRecipesViewController;
    private SearchRecipeViewController searchRecipeViewController;
    private FavoriteRecipeViewController favoriteRecipeViewController;

    private Recipe currentRecipe;
    private ShoppingList currentShoppingList;

    /**
     * Affiche l'écran d'accueil des recettes
     */
    public void displayHomeRecipe() {
        this.currentShoppingList = null;
        this.currentRecipe = null;
        FXMLLoader loader = loadFXML("HomeRecipe.fxml");
        HomeRecipeViewController viewController = loader.getController();
        viewController.setListener(this);
    }


    /**
     * Convertit une Liste de {@link Product} en un {@link String} lisible par des humains
     * @return le String construit
     */
    public static String productListToString(Recipe currentRecipe) {
        StringBuilder res = new StringBuilder();
        for (Product p : currentRecipe) {
            res.append(p.getQuantity());
            res.append(p.getNameUnity()).append(" ");
            res.append(p.getName()).append("\n");
        }
        return res.toString();
    }
    // <-------------------------- Écran d'accueil des Recettes --------------------------> \\

    /**
     * Affiche l'écran permettant à l'utilisateur de voir ses recettes
     */
    @Override
    public void onUserRecipesButtonClick() {
        UserRecipesController userRecipesController = new UserRecipesController(currentStage,this,null);
        userRecipesController.displayUserRecipes();
    }

    /**
     * Affiche l'écran permettant de créer une nouvelle recette
     */
    @Override
    public void onNewRecipeButtonClick() {
        CreateRecipeController createRecipeController = new CreateRecipeController(currentStage,this);
        createRecipeController.displayCreateRecipe();
    }

    /**
     * Retourne à l'écran principal
     */
    @Override
    public void onBackButtonClick() {
        //parentController.displayMain(); TODO
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayHome();
    }


    @Override
    public void logout() {
        userLogout();
    }


    @Override
    public void onFavoriteRecipe() {
        FavoriteRecipesController favoriteRecipesController = new FavoriteRecipesController(currentStage,this);
        favoriteRecipesController.displayFavoriteRecipe();
    }

    /***************************FAVORITERECIPE*******************************/




    @Override
    public void onReturn() { displayHomeRecipe(); }
}
