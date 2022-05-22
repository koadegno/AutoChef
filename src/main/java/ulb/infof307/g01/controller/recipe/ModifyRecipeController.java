package ulb.infof307.g01.controller.recipe;

import javafx.stage.Stage;
import ulb.infof307.g01.controller.ListenerBackPreviousWindow;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.ViewController;

import java.sql.SQLException;
import java.util.List;

public class ModifyRecipeController extends CreateRecipeController {
    private ListenerGetRecipe listenerGetRecipe;

    public ModifyRecipeController(ShoppingList currentShoppingList, Stage primaryStage, ListenerBackPreviousWindow listenerBackPreviousWindow, ListenerGetRecipe listenerGetRecipe) {
        super(primaryStage, listenerBackPreviousWindow);
        this.currentShoppingList = currentShoppingList;
        this.listenerGetRecipe = listenerGetRecipe;

    }

    public void onSubmitButtonClick(String diet, String type, int nbPerson, String preparation, String recipeName)  {
        if(!isValidRecipe(diet, type, nbPerson, preparation, recipeName)) return;
        //Récupérer les informations de l'ancienne recette : ID + Favorite
        int idRecipe = currentRecipe.getId();
        boolean favoris = currentRecipe.getFavorite();
        //Creation d'une nouvelle recette
        currentRecipe = new Recipe.RecipeBuilder().withName(recipeName).withNumberOfPerson(nbPerson).withType(type).withPreparation(preparation).withCategory(diet).build();
        currentRecipe.addAll(currentShoppingList);
        try {
            currentRecipe.setId(idRecipe);
            currentRecipe.setFavorite(favoris);
            configuration.getRecipeDao().update(currentRecipe);
        } catch (SQLException e) {
            ViewController.showErrorSQL();
        }

        if (listenerGetRecipe != null ) {
            listenerGetRecipe.setRecipe(currentRecipe);
        }
        listenerBackPreviousWindow.onReturn();
    }

    public void prefillFields(Recipe currentRecipe, List<Product> productList) {
        this.currentRecipe = currentRecipe;
        createRecipeViewController.prefillFields(currentRecipe.getName(), currentRecipe.getPreparation(),
                currentRecipe.getType(), currentRecipe.getCategory(),
                currentRecipe.getNbrPerson(), productList);
        createRecipeViewController.setCancelButtonToModifyRecipe();
    }

    @FunctionalInterface
    public interface ListenerGetRecipe{
        void setRecipe(Recipe recipe);
    }

}
