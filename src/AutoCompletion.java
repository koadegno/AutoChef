import java.util.*;

public class AutoCompletion {

    static LinkedList<Recipe> generateMenu (LinkedList<Recipe> prec, int nbRecipes) {

        LinkedList<Recipe> menu = new LinkedList<Recipe>();

        HashMap<String, Integer> recipes = new HashMap<String, Integer>();

        //Ajouter au dico les category, 0

        for (Recipe recipe : prec) {
            String category = recipe.getCategory();
            int val = recipes.get(category);
            recipes.replace(category, ++val);
        }

        for (int remainingRecipes = 0; remainingRecipes < nbRecipes; remainingRecipes++) {

            String categoryMin = findMin(recipes);
            // LinkedList<Recipe> query = Query BDD
            Recipe choice = choiceRecipe(query);
            // menu.add(choice);
            int val = recipes.get(categoryMin);
            recipes.replace(categoryMin, ++val);
        }
        return menu;
    }

    static String findMin (HashMap<String, Integer> dico) {
        Map.Entry<String, Integer> min = null;
        for (Map.Entry<String, Integer> entry : dico.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min.getKey();
    }

    static Recipe choiceRecipe(LinkedList<Recipe> recipes, LinkedList<Recipe> prec) {

        for (Recipe recipe: recipes) {
            Boolean notFound = true;
            for (Recipe choice: prec) {
                if (choice.equals(recipe)) {
                    notFound = false;
                    break;
                }
            }
            if (notFound) {
                return recipe;
            }
        }

        Random random = new Random();
        int nb;
        nb = random.nextInt(recipes.size());
        return recipes.get(nb);
    }
}
