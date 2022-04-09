package ulb.infof307.g01.model;
import ulb.infof307.g01.db.Configuration;

import java.sql.SQLException;
import java.util.*;


/**
 * Classe static permettant de générer une liste de recettes
 */

public class AutoCompletion {

    /**
     *  Méthode static permettant générer une liste de recette à partir d'une base de donnée
     * @param recipesAlreadyUsed Liste contenant les recettes déjà utilisées
     * @param categoriesWanted   HashMap contenant les categories souhaitées et en quelle quantité
     * @param nbRecipes          int indiquant le nombre de recettes souhaitées
     * @param type               String indiquant le type (dessert, boisson, etc.) de recette souhaitée
     */

    static public List<Recipe> generateRecipesList(List<Recipe> recipesAlreadyUsed, Map<String, Integer> categoriesWanted, int nbRecipes, String type) throws SQLException {

        ArrayList<Recipe> menu = new ArrayList<>();
        if (categoriesWanted.isEmpty()){ return menu;}

        for (int remainingRecipes = 0; remainingRecipes < nbRecipes; remainingRecipes++) {

            String categoryMax = null;
            ArrayList<Recipe> recipes =  new ArrayList<>();

            while (recipes.size() == 0) {
                categoryMax = findMax(categoriesWanted);
                recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(categoryMax, type,  0);

                // Si aucune recette trouvée correspondante à la catégorie souhaitée, la catégorie est supprimée
                if (recipes.size() == 0) {
                    categoriesWanted.remove(categoryMax);
                    // Si parmi les catégories souhaitées aucune recette n'est trouvée, on retourne une liste vide
                    if (categoriesWanted.isEmpty()){ return menu;}
                }
            }

            // Ajoute la recette trouvée et décrémente sa catégorie
            Recipe choice = choiceRecipe(recipes, recipesAlreadyUsed);
            menu.add(choice);
            recipesAlreadyUsed.add(choice);
            int val = categoriesWanted.get(categoryMax);
            categoriesWanted.replace(categoryMax, --val);
        }
        return menu;
    }


    /**
     * Méthode static renvoyant la clé ayant la valeur la plus haute
     * @return  String de la key ayant la valeur la plus haute
     */

    static public String findMax (Map<String, Integer> categoriesRecipesUsed) {

        if (categoriesRecipesUsed.isEmpty()) {return null;}

        Map.Entry<String, Integer> max = null;
        for (Map.Entry<String, Integer> entry : categoriesRecipesUsed.entrySet()) {
            if (max == null || max.getValue() < entry.getValue()) {
                max = entry;
            }
        }
        return max.getKey();
    }


    /**
     * Méthode static essayant de renvoyer une recette inutilisée
     * @param   recipes             Liste contenant les recettes à choisir
     * @param   recipesAlreadyUsed  Liste contenant les recettes déjà utilisées
     */

    static public Recipe choiceRecipe(List<Recipe> recipes, List<Recipe> recipesAlreadyUsed) {

        if (recipes.size() == 0) { return null;}

        // Parcours les recettes à la recherche de recette inutilisée
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

        // Si toutes les recettes sont déjà utilisées en renvoie une random
        Random random = new Random();
        int index = random.nextInt(recipes.size());
        return recipes.get(index);
    }
}
