package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.view.recipe.*;


/**
 * Contrôleur responsable de tous les écrans en lien avec les recettes
 * Permet d'accéder à la création d'une recette, la visualisation des toutes ses recettes et de ses recettes favorites
 */
public class HomePageRecipeController extends Controller implements HomeRecipeViewController.HomeRecipeListener, ListenerBackPreviousWindow {

    /**
     * Affiche l'écran d'accueil des recettes
     */
    public void displayHomeRecipe() {
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
    public void onBackButton() {
        HomePageController homePageController = new HomePageController(currentStage);
        homePageController.displayHome();
    }


    @Override
    public void logout() {
        userLogout();
    }


    @Override
    public void onFavoriteRecipeButtonClick() {
        FavoriteRecipesController favoriteRecipesController = new FavoriteRecipesController(currentStage,this);
        favoriteRecipesController.displayFavoriteRecipe();
    }

    @Override
    public void onReturn() { displayHomeRecipe(); }


}
