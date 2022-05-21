package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.dao.RecipeCategoryDao;
import ulb.infof307.g01.model.database.dao.RecipeDao;
import ulb.infof307.g01.model.database.dao.RecipeTypeDao;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.SearchRecipeViewController;
import ulb.infof307.g01.view.recipe.UserRecipesViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui contrôle l'affichage de toutes les recettes
 * Permet d'appliquer un filtre sur les recettes que l'on souhaite visualiser
 */
public class SearchRecipeController extends Controller implements SearchRecipeViewController.Listener {

    private SearchRecipeViewController searchRecipeViewController;

    private SearchRecipeListener listener;


    public SearchRecipeController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow){
        super(listenerBackPreviousWindow);
        setStage(primaryStage);
    }

    public void displaySearchRecipe(){
        FXMLLoader loader = loadFXML("SearchRecipe.fxml");
        searchRecipeViewController = loader.getController();
        searchRecipeViewController.setListener(this);

        try {
            RecipeDao recipeDao = configuration.getRecipeDao();
            List<Recipe> recipesList = recipeDao.getRecipeWhere(null, null, 0);
            RecipeTypeDao recipeTypeDao = configuration.getRecipeTypeDao();
            List<String> typesList = recipeTypeDao.getAllName();
            RecipeCategoryDao recipeCategoryDao = configuration.getRecipeCategoryDao();
            List<String> dietsList = recipeCategoryDao.getAllName();
            searchRecipeViewController.initialize(dietsList, typesList, recipesList);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    public void setListener(SearchRecipeListener searchRecipeListener){
        this.listener = searchRecipeListener;
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onTypeComboBoxSelected(String recipeType) {
        String recipeDiet = searchRecipeViewController.getDietComboBoxSelectedItem();
        int recipeNbPerson = searchRecipeViewController.getNbPersonSpinnerValue();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onDietComboBoxSelected(String recipeDiet) {
        String recipeType = searchRecipeViewController.getTypeComboBoxSelectedItem();
        int recipeNbPerson = searchRecipeViewController.getNbPersonSpinnerValue();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    /**
     * Active le {@link javafx.scene.control.Spinner} permettant de filtrer en fonction
     * d'un nombre de personnes
     */
    @Override
    public void onNbPersonCheckBoxChecked(boolean isChecked) {
        searchRecipeViewController.setDisableNbPersonSpinner(!isChecked);

        onNbPersonSpinnerClicked(searchRecipeViewController.getNbPersonSpinnerValue());
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onNbPersonSpinnerClicked(int recipeNbPerson) {
        String recipeType = searchRecipeViewController.getTypeComboBoxSelectedItem();
        String recipeDiet = searchRecipeViewController.getDietComboBoxSelectedItem();

        refreshRecipeList(recipeType, recipeDiet, recipeNbPerson);
    }

    /**
     * Filtre les recettes affichées en fonction du régime, de la catégorie du plat
     * et du nombre de personnes sélectionnés
     */
    @Override
    public void onNbPersonSpinnerKeyPressed(int recipeNbPerson) {
        onNbPersonSpinnerClicked(recipeNbPerson);
    }

    /**
     * Rafraichit la liste des recettes affichées en fonction du régime, de la catégorie du plat et du nombre
     * de personnes sélectionnées
     * @param recipeType le type de {@link Recipe} selon lequelle filtrer
     * @param recipeDiet le régime de {@link Recipe} selon lequelle filtrer
     * @param nbPerson le nombre de personnes de {@link Recipe} selon lequelle filtrer
     */
    private void refreshRecipeList(String recipeType, String recipeDiet, int nbPerson) {
        try {
            List<Recipe> recipesList = configuration.getRecipeDao().getRecipeWhere(recipeDiet, recipeType, nbPerson);
            searchRecipeViewController.refreshRecipesTableView(recipesList);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
    }

    /**
     * Affiche une recette sélectionnée
     * @see UserRecipesViewController
     */
    @Override
    public void onRecipesTableViewClicked(Recipe selectedRecipe) {

        listenerBackPreviousWindow.onReturn();
        listener.onRecipeSelected(selectedRecipe);
    }

    @Override
    public void onCancelSearchButton() {
        listenerBackPreviousWindow.onReturn();
    }

    @FunctionalInterface
    public interface SearchRecipeListener{
        void onRecipeSelected(Recipe selectedRecipe);
    }

}
