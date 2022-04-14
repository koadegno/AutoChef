package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.db.Configuration;
import ulb.infof307.g01.view.HomePageController;
import ulb.infof307.g01.view.ViewController;
import ulb.infof307.g01.view.recipe.CreateRecipeViewController;

import java.sql.SQLException;
import java.util.Objects;

public class RecipeController extends Controller implements CreateRecipeViewController.CreateRecipeListener {

    private CreateRecipeViewController createRecipeViewController; //TODO
    private Recipe currentRecipe;

    @Override
    public void onSubmitButton(int dietIndex, int typeIndex, int nbPerson, String preparation, String recipeName) {
        if (dietIndex < 0)
            createRecipeController.dietComboBoxError();
        if (typeIndex < 0)
            createRecipeController.typeComboBoxError();
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

    }

    @Override
    public void onCancelButton() {

    }
}
