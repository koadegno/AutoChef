package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectedUserTest {
    ConnectedUser myConnectedUser;

    @BeforeEach
    void createUser(){
        myConnectedUser = new ConnectedUser();
    }
    @Test
    void addFavoriteRecipe() {
        int favoriteRecipeNumber = 2;
        Recipe lovedRecipe = new Recipe("Ratatouille");
        Recipe lovedRecipe2 = new Recipe("Tiramisu");
        myConnectedUser.addFavoriteRecipe(lovedRecipe);
        myConnectedUser.addFavoriteRecipe(lovedRecipe2);
        myConnectedUser.addFavoriteRecipe(lovedRecipe);
        assertEquals(myConnectedUser.getFavoriteRecipes().size(), favoriteRecipeNumber);
    }

    @Test
    void addCreatedShoppingList() {
        int createdShopList = 2;
        ShoppingList shoplist = new ShoppingList("Liste de course pour gateau au chocolat");
        ShoppingList shoplist2 = new ShoppingList("Liste de course pour ramen");
        myConnectedUser.addCreatedShoppingList(shoplist);
        myConnectedUser.addCreatedShoppingList(shoplist2);
        myConnectedUser.addCreatedShoppingList(shoplist);
        assertEquals(myConnectedUser.getShoppingListHistory().size(), createdShopList);
    }
}