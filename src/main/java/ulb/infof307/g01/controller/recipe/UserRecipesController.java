package ulb.infof307.g01.controller.recipe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.controller.help.HelpController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.dao.RecipeDao;
import ulb.infof307.g01.model.export.JSON;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.UserRecipesViewController;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui contrôle l'affichage d'une recette
 * Permet de modifier cette recette, et de la visualiser
 */
public class UserRecipesController extends Controller implements UserRecipesViewController.UserRecipesListener, ListenerBackPreviousWindow, SearchRecipeController.SearchRecipeListener, ModifyRecipeController.ListenerGetRecipe {

    Scene sceneViewRecipe = null;

    boolean isWaitingModification = false;

    private UserRecipesViewController userRecipesViewController;

    private Recipe currentRecipe;
    private ShoppingList currentShoppingList;

    public UserRecipesController(ListenerBackPreviousWindow listenerBackPreviousWindow,Recipe currentRecipe){
        super(listenerBackPreviousWindow);
        this.currentRecipe = currentRecipe;
    }
    public void displayUserRecipes(){
        FXMLLoader loader = this.loadFXML("Recipe.fxml");
        userRecipesViewController = loader.getController();
        userRecipesViewController.setListener(this);
        onRecipeSelected(currentRecipe);
    }
    /**
     * Cherche si une recette existe dans la base de données et l'affiche si possible, sinon affiche une erreur
     * sur le champ de recherche
     * @param recipeName Nom de la recette à rechercher
     */
    @Override
    public void onRecipeSearchTextFieldSubmit(String recipeName) {
        if (recipeName.isBlank())
            userRecipesViewController.recipeSearchTextFieldError(true);

        try {
            RecipeDao recipeDao = configuration.getRecipeDao();
            currentRecipe = recipeDao.get(recipeName);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }

        if (currentRecipe == null)
            userRecipesViewController.recipeSearchTextFieldError(true);
        else {
            userRecipesViewController.recipeSearchTextFieldError(false);
            userRecipesViewController.setDisableRecipeButtons(false);
            userRecipesViewController.setRecipeTextArea(currentRecipe.getName(), HomePageRecipeController.productListToString(currentRecipe),
                    currentRecipe.getPreparation());
            userRecipesViewController.checkFavoriteCheckBox(currentRecipe.isFavorite());
        }

    }

    /**
     * Afficher l'écran permettant de modifier une recette déjà existante
     */
    @Override
    public void onModifyRecipeButtonClick() {
        isWaitingModification = false;

        this.sceneViewRecipe = currentStage.getScene();
        // TODO  VOIR comment communiquer avec createRecipeView

        List<Product> productList = new ArrayList<>(currentRecipe);
        this.currentShoppingList = new ShoppingList(currentRecipe.getName());
        currentShoppingList.addAll(productList);

        ModifyRecipeController modifyRecipeController = new ModifyRecipeController(currentShoppingList,currentStage,this, this);
        modifyRecipeController.displayCreateRecipe();
        modifyRecipeController.prefillFields(currentRecipe,productList);
    }

    /**
     * Supprime une recette de la Base de donnée
     */
    @Override
    public void onDeleteRecipeButtonClick() {
        ButtonType alertResult = ViewController.showAlert(Alert.AlertType.CONFIRMATION, "Supprimer la recette ?", "Etes vous sur de vouloir supprimer la recette ? ");
        if(alertResult == ButtonType.OK){
            try {
                configuration.getRecipeDao().delete(currentRecipe);
            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }
            userRecipesViewController.setDisableRecipeButtons(true);
            userRecipesViewController.resetRecipeTextArea();
            currentRecipe = null;
        }

    }

    /**
     * Affiche l'écran permettant de sélectionner une recette parmis toutes celles existantes
     */
    @Override
    public void onSeeAllRecipesButtonClick() {
        SearchRecipeController searchRecipeController = new SearchRecipeController(currentStage,this);
        searchRecipeController.setListener(this);
        searchRecipeController.displaySearchRecipe();
    }

    /**
     * Affiche une {@code pop-up} permettant d'importer une recette depuis un fichier {@code JSON}
     * @see JSON
     */
    @Override
    public void onImportRecipeFromJSONButtonClick() {
        final String windowTitle = "Importer une Recette depuis un fichier JSON";
        File jsonRecipe = importJSON(windowTitle);

        if(jsonRecipe != null){
            JSON json = new JSON();
            try {
                json.importRecipe(jsonRecipe.getAbsolutePath());
            } catch (SQLException | ParseException e ) {
                String messageError = "Le contenu du JSON est incorrecte";
                UserRecipesViewController.showAlert(Alert.AlertType.ERROR, "Erreur", messageError);
            } catch (IOException e) {
                String messageError = "Le fichier n'a pas pu être ouvert";
                UserRecipesViewController.showAlert(Alert.AlertType.ERROR, "Erreur", messageError);
            }
            onRecipeSearchTextFieldSubmit(json.getNameRecipe());
        }

    }

    /**
     * Revient à la page d'accueil des recettes
     */
    @Override
    public void onBackToHomeRecipeButtonClick() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void logout() {
        userLogout();
    }

    @Override
    public void onEndViewFavoriteRecipeButton() {
        listenerBackPreviousWindow.onReturn();
    }

    @Override
    public void onFavoriteRecipeCheck(Boolean isChecked) {
        currentRecipe.setFavorite(isChecked);
        try {
            configuration.getRecipeDao().update(currentRecipe);
        } catch (SQLException e) {
            UserRecipesViewController.showErrorSQL();
        }
    }

    @Override
    public void onReturn() {
        displayUserRecipes();
    }

    public void initReadOnlyRecipeController() {
        userRecipesViewController.initReadOnlyMode();
        userRecipesViewController.setRecipeTextArea(currentRecipe.getName(), HomePageRecipeController.productListToString(currentRecipe),
                currentRecipe.getPreparation());
    }

    @Override
    public void onRecipeSelected(Recipe selectedRecipe) {
        currentRecipe = selectedRecipe;
        if (currentRecipe != null) {
            userRecipesViewController.recipeSearchTextFieldError(false);
            userRecipesViewController.setDisableRecipeButtons(false);
            userRecipesViewController.setVisibleHelpRecipe(false);
            userRecipesViewController.setRecipeTextArea(currentRecipe.getName(), HomePageRecipeController.productListToString(currentRecipe), currentRecipe.getPreparation());
            userRecipesViewController.checkFavoriteCheckBox(currentRecipe.isFavorite());
        }
    }

    @Override
    public void setRecipe(Recipe recipe) {
        currentRecipe = recipe;
    }

    @Override
    public void helpModifyRecipeClick() {
        int numberOfImageHelp = 9;
        String directory = "helpModifyRecipe/";
        HelpController helpController = new HelpController(directory, numberOfImageHelp);
        helpController.displayHelpShop();
    }
}
