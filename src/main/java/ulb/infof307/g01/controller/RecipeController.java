package ulb.infof307.g01.controller;

import ulb.infof307.g01.view.recipe.CreateRecipeController;

import java.util.Objects;

public class RecipeController extends Controller implements CreateRecipeController.CreateRecipeListener {

    private CreateRecipeController createRecipeController; //TODO
    @Override
    public void onSubmitButton(int dietIndex, int typeIndex, int nbPerson, String preparation, String recipeName) {
        if (dietIndex < 0)
            createRecipeController.dietComboBoxError();
        if (typeIndex < 0)
            createRecipeController.typeComboBoxError();
        // TODO Vérifier que la liste d'ingrédients n'est pas vide
        if (Objects.equals(preparation, ""))
            createRecipeController.preparationTextAreaError();
        if(Objects.equals(recipeName, ""))
            createRecipeController.recipeNameTextFieldError();

    }

    @Override
    public void onModifyProductsButton() {

    }

    @Override
    public void onCancelButton() {

    }
}
