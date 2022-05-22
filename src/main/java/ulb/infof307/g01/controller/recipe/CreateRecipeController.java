package ulb.infof307.g01.controller.recipe;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.view.ViewController;

import java.sql.SQLException;

public class CreateRecipeController extends EditRecipeController {


    public CreateRecipeController(Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow) {
        super(primaryStage, listenerBackPreviousWindow);
    }

    public void onSubmitButtonClick(String diet, String type, int nbPerson, String preparation, String recipeName)  {
        if(!isValidRecipe(diet, type, nbPerson, preparation, recipeName)) return;
        //Creation d'une nouvelle recette
        currentRecipe = new Recipe.RecipeBuilder().withName(recipeName).withNumberOfPerson(nbPerson).withType(type).withPreparation(preparation).withCategory(diet).build();
        currentRecipe.addAll(currentShoppingList);
        try {
            configuration.getRecipeDao().insert(currentRecipe);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }
        listenerBackPreviousWindow.onReturn();
    }
}
