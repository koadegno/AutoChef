package ulb.infof307.g01.model;

import java.util.ArrayList;

public class ConnectedUser extends User{
    private ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
    private ArrayList<ShoppingList> shoppingListHistory = new ArrayList<>();
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
    private ArrayList<Menu> menus = new ArrayList<>();
    private ArrayList<Shop> shops = new ArrayList<>();


    public void addFavoriteRecipe(Recipe recipe){
        for (Recipe favoriteRecipe:favoriteRecipes) { //pas de doublons
            if (recipe.equals(favoriteRecipe))return;
        }
        favoriteRecipes.add(recipe);
    }
    public void addCreatedShoppingList(ShoppingList createdShoppingList){ //pas de doublons
        for (ShoppingList shoppingList:shoppingListHistory) {
            if (shoppingList.equals(createdShoppingList))return;
        }
        shoppingListHistory.add(createdShoppingList);
    }


    //getters and setters TODO: supprimer celles qui ne sont pas utilisés
    public ArrayList<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public ArrayList<ShoppingList> getShoppingListHistory() {
        return shoppingListHistory;
    }

    public void setFavoriteRecipes(ArrayList<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public void setShoppingListHistory(ArrayList<ShoppingList> shoppingListHistory) {
        this.shoppingListHistory = shoppingListHistory;
    }

}
