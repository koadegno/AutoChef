package ulb.infof307.g01.controller.shoppingList;

import ulb.infof307.g01.controller.recipe.RecipeController;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.shoppingList.ModifyShoppingListViewController;

public class RecipeShoppingListController extends ShoppingListController {
    private final RecipeController recipeController;

    public RecipeShoppingListController(ShoppingList shoppingList, RecipeController recipeController) {
        super(null);
        modifyShoppingListViewController = new ModifyShoppingListViewController();
        modifyShoppingListViewController.setListener(this);
        this.recipeController = recipeController;
        initForCreateRecipe(shoppingList);
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
        recipeController.modifyProductsCallback(null);
    }

    @Override
    public void returnAddedProducts() {
        recipeController.modifyProductsCallback(shoppingListToSend);
    }

}
