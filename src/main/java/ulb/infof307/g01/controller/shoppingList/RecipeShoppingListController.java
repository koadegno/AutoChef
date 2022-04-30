package ulb.infof307.g01.controller.shoppingList;

import ulb.infof307.g01.controller.recipe.RecipeController;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewController;

public class RecipeShoppingListController extends ShoppingListController {
    private final RecipeController recipeController;

    public RecipeShoppingListController(RecipeController recipeController) {
        super(new UserShoppingListViewController(), null);
        this.recipeController = recipeController;
    }

    /**
     * Remplis la tableView des ingrédients d'une recette
     * @param shoppingList liste de produits
     */
    public void initForCreateRecipe(ShoppingList shoppingList) {
        shoppingListToSend = new ShoppingList("temporary");
        loadFXML(userShoppingListViewController, "ShoppingList.fxml");
        userShoppingListViewController.setListener(this);
        userShoppingListViewController.initForCreateRecipe(shoppingList);
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
