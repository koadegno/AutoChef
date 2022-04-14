package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;
import ulb.infof307.g01.view.recipe.HomeRecipeViewController;

import java.sql.SQLException;

public class RecipeController extends Controller implements HomeRecipeViewController.HomeRecipeListener, CreateRecipeViewController.CreateRecipeListener {

    private Controller parentController;
    private CreateRecipeViewController createRecipeViewController; //TODO
    private Recipe currentRecipe;

    public void displayMain() {
        FXMLLoader loader = loadFXML("HomeRecipe.fxml");
        HomeRecipeViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    // <-------------------------- Écran d'accueil des Recettes --------------------------> \\

    @Override
    public void onUserRecipesButtonClick() {
        FXMLLoader loader = this.loadFXML("viewRecipe.fxml");
        // TODO: ViewController UserRecipes
        viewController.setListener(this);
    }

    @Override
    public void onNewRecipeButtonClick() {
        FXMLLoader loader = this.loadFXML("viewRecipe.fxml");
        CreateRecipeViewController viewController = loader.getController();
        viewController.setListener(this);
    }

    @Override
    public void onBackButtonClick() {
        parentController.displayMain();
    }

    // <-------------------------- Écran de Création des Recettes --------------------------> \\

    @Override
    public void onSubmitButton(String diet, String type, int nbPerson, String preparation, String recipeName) {

        boolean isValid = isValidRecipe(diet, type, nbPerson, preparation, recipeName);

        if (isValid) {
            currentRecipe = new Recipe(recipeName);
            currentRecipe.setCategory(diet);
            currentRecipe.setPreparation(preparation);
            currentRecipe.setType(type);
            currentRecipe.setNbrPerson(nbPerson);

            try {
                Configuration.getCurrent().getRecipeDao().insert(currentRecipe);
            } catch (SQLException e) {
                ViewController.showErrorSQL();
            }

            parentController.displayMain();

        }
    }

    /**
     * Vérifie que les paramètres sont valides pour créer un objet {@link Recipe}
     * @return {@code true} si les paramètres sont valides, {@code false} sinon
     */
    private boolean isValidRecipe(String diet, String type, int nbPerson, String preparation, String recipeName) {
        boolean isValid = true;
        // Vérifie qu'un Régime a été sélectionné
        if (diet == null) {
            createRecipeViewController.dietComboBoxError();
            isValid = false;
        }
        // Vérifie qu'un type a été sélectionné
        if (type == null) {
            createRecipeViewController.typeComboBoxError();
            isValid = false;
        }
        // TODO Vérifier que la liste d'ingrédients n'est pas vide

        // Vérifie que la préparation n'est pas vide
        if (preparation.isBlank()) {
            createRecipeViewController.preparationTextAreaError();
            isValid = false;
        }
        // Vérifie que le nom n'est pas vide
        if(recipeName.isBlank()) {
            createRecipeViewController.recipeNameTextFieldError();
            isValid = false;
        }
        // Vérifie que le nombre de personnes est supérieur à 0
        if (nbPerson < 1) {
            createRecipeViewController.nbPersonSpinnerError();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onModifyProductsButton() {
        // TODO: Connecter au nouveau WindowUserShoppingListsController
    }

    @Override
    public void onCancelButton() {
        displayMain();
    }
}
