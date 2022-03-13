package ulb.infof307.g01.cuisine;
import ulb.infof307.g01.db.Database;

import java.sql.SQLException;
import java.util.*;

public class AutoCompletion {

    static public List<Recipe> generateMenu (List<Recipe> recipesAlreadyUsed, HashMap<String, Integer> categoriesWanted, int nbRecipes, String type, Database db) throws SQLException {

        ArrayList<Recipe> menu = new ArrayList<>();
        if (categoriesWanted.isEmpty()){ return menu;}

        for (int remainingRecipes = 0; remainingRecipes < nbRecipes; remainingRecipes++) {

            String categoryMax = null;
            ArrayList<Recipe> recipes =  new ArrayList<>();

            while (recipes.size() == 0) {
                categoryMax = findMax(categoriesWanted);
                recipes     = db.getRecipeWhere(categoryMax, type,  0);

                if (recipes.size() == 0) {
                    categoriesWanted.remove(categoryMax);
                    if (categoriesWanted.isEmpty()){ return menu;}
                }
            }

            Recipe choice = choiceRecipe(recipes, recipesAlreadyUsed);
            menu.add(choice);
            recipesAlreadyUsed.add(choice);
            int val = categoriesWanted.get(categoryMax);
            categoriesWanted.replace(categoryMax, --val);
        }
        return menu;
    }


    static public String findMax (HashMap<String, Integer> categoriesRecipesUsed) {

        if (categoriesRecipesUsed.isEmpty()) {return null;}

        Map.Entry<String, Integer> max = null;
        for (Map.Entry<String, Integer> entry : categoriesRecipesUsed.entrySet()) {
            if (max == null || max.getValue() < entry.getValue()) {
                max = entry;
            }
        }
        return max.getKey();
    }

    static public Recipe choiceRecipe(List<Recipe> recipes, List<Recipe> recipesAlreadyUsed) {

        if (recipes.size() == 0) { return null;}

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
