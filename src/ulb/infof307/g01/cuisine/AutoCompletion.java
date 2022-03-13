package ulb.infof307.g01.cuisine;
import ulb.infof307.g01.db.Database;

import java.sql.SQLException;
import java.util.*;

public class AutoCompletion {

    static public List<Recipe> generateMenu (List<Recipe> recipesAlreadyUsed, int nbRecipes, Database db) throws SQLException {

        ArrayList<Recipe> menu = new ArrayList<>();
        HashMap<String, Integer> categoriesRecipesUsed = new HashMap<>();
        ArrayList<String> categories = db.getAllCategories();

        for (String category : categories) {
            categoriesRecipesUsed.put(category, 0);
        }

        for (Recipe recipe : recipesAlreadyUsed) {
            String category = recipe.getCategory();
            int val = categoriesRecipesUsed.get(category);
            categoriesRecipesUsed.replace(category, ++val);
        }

        for (int remainingRecipes = 0; remainingRecipes < nbRecipes; remainingRecipes++) {

            String categoryMin = findMin(categoriesRecipesUsed);
            ArrayList<Recipe> recipes = db.getRecipeWhere(categoryMin, null,  0);
            Recipe choice = choiceRecipe(recipes, recipesAlreadyUsed);
            menu.add(choice);
            recipesAlreadyUsed.add(choice);
            int val = categoriesRecipesUsed.get(categoryMin);
            categoriesRecipesUsed.replace(categoryMin, ++val);
        }

        return menu;
    }


    static public String findMin (HashMap<String, Integer> categoriesRecipesUsed) {

        Map.Entry<String, Integer> min = null;
        for (Map.Entry<String, Integer> entry : categoriesRecipesUsed.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min.getKey();
    }

    static public Recipe choiceRecipe(List<Recipe> recipes, List<Recipe> recipesAlreadyUsed) {

        for (Recipe recipe: recipes) {
            boolean notFound = true;
            for (Recipe choice: recipesAlreadyUsed) {
                if (choice.equals(recipe)) {
                    notFound = false;
                    break;
                }
            }

            if (notFound) { return recipe;}
        }

        Random random = new Random();
        int nb = random.nextInt(recipes.size());
        return recipes.get(nb);
    }
}
