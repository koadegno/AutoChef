package ulb.infof307.g01.view.recipe;


import ulb.infof307.g01.view.ViewController;

/**
 * La classe gère la vue de la page d'accueil pour les recettes
 */
public class HomeRecipeViewController extends ViewController<HomeRecipeViewController.HomeRecipeListener> {

    public void onUserRecipesButtonClick() {listener.onUserRecipesButtonClick();}
    public void onCreateRecipeButtonClick() {listener.onNewRecipeButtonClick();}
    public void onBackButtonClick() {listener.onBackButtonClick();}

    public void logout() {
        listener.logout();
    }

    public void onFavoriteRecipe() {
        listener.onFavoriteRecipe();
    }

    public interface HomeRecipeListener {
        void onUserRecipesButtonClick();
        void onNewRecipeButtonClick();
        void onBackButtonClick();

        void logout();

        void onFavoriteRecipe();
    }
}
