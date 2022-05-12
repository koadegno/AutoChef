package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.recipe.FavoriteRecipeViewController;
import java.util.List;

/**
 * Classe qui contrôle les recettes favorites d'un utilisateur
 * Permet d'afficher une liste des recettes favorites + de visualiser une recette sélectionnée
 */
public class FavoriteRecipesController extends Controller implements FavoriteRecipeViewController.FavoriteRecipesListener,ListenerBackPreviousWindow {

    Scene sceneFavoriteRecipe = null;
    private FavoriteRecipeViewController favoriteRecipeViewController;
    private Recipe currentRecipe;


    public FavoriteRecipesController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
    }

    public void displayFavoriteRecipe(){
        FXMLLoader loader = this.loadFXML("FavoriteRecipe.fxml");
        favoriteRecipeViewController = loader.getController();
        favoriteRecipeViewController.setListener(this);
        List<Recipe> userFavoriteRecipe = Configuration.getCurrent().getRecipeDao().getFavoriteRecipes();
        favoriteRecipeViewController.displayFavoriteRecipe(userFavoriteRecipe);
    }
    @Override
    public void onFavoriteRecipesTableViewClicked(Recipe recipe) {
        currentRecipe = recipe;
        sceneFavoriteRecipe = currentStage.getScene();
        UserRecipesController userRecipesController = new UserRecipesController(currentStage,this,currentRecipe);
        userRecipesController.displayUserRecipes();
        userRecipesController.initReadOnlyRecipeController();
    }


    @Override
    public void logout() {
        userLogout();
    }

    @Override
    public void onCancelButton()  { listenerBackPreviousWindow.onReturn(); }

    @Override
    public void onReturn() {
        displayFavoriteRecipe();
    }
}
