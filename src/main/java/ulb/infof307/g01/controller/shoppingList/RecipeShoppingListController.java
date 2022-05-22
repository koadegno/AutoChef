package ulb.infof307.g01.controller.shoppingList;

import ulb.infof307.g01.controller.recipe.EditRecipeController;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.shoppingList.ModifyShoppingListViewController;

/**
 * Contrôleur de la page affichant les recettes sur la page d'une liste de course
 */
public class RecipeShoppingListController extends ShoppingListController {

    private final EditRecipeController createRecipeController;

    public RecipeShoppingListController(EditRecipeController createRecipeController) {
        super(null);
        modifyShoppingListViewController = new ModifyShoppingListViewController();
        modifyShoppingListViewController.setListener(this);
        this.createRecipeController = createRecipeController;

    }

    /**
     * Remplis la tableView des ingrédients d'une recette
     * @param shoppingList liste de produits
     */
    public void initForCreateRecipe(ShoppingList shoppingList) {
        shoppingListToSend = new ShoppingList("temporary");
        loadFXML(modifyShoppingListViewController, "ShoppingList.fxml");
        modifyShoppingListViewController.setListener(this);
        modifyShoppingListViewController.initForCreateRecipe(shoppingList);
        initInformationShoppingList(false);
    }

    @Override
    public void cancelRecipeCreation() {
        createRecipeController.modifyProductsCallback(null);
    }

    @Override
    public void returnAddedProducts() {
        createRecipeController.modifyProductsCallback(shoppingListToSend);
    }

}
