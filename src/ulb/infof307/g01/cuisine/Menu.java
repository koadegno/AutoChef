package ulb.infof307.g01.cuisine;
import ulb.infof307.g01.db.Database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Collections;

/**
* Classe représentant un Menu Hebdomadaire
*
*/
public class Menu {

    private String name;
    private final int nbOfdays = 7;
    private Vector<Vector<Recipe>> menu = new Vector<>(nbOfdays);

    public Menu(String name) {
        this.name = name;

        initVector();
    }

    public Menu(){
        initVector();
    }

    private void initVector() {
        menu = new Vector<>(10);
        for (int i = 0; i<nbOfdays; i++) {
            menu.add(new Vector<>());
        }
    }

    public void setName(String name){this.name = name;}

    /**
     * @return le nombre de {@code Recipe} contenues dans le Menu (pour tout les jours)
     * @see Recipe
     */
    public int size() {
        int size = 0;
        for(Vector<Recipe> vector : menu){
            size += vector.size();
        }
        return size;
    }

    public int getNbOfdays() { return nbOfdays;}

    /** @see Day
     * @see Recipe
     * @param day le jour pour lequel il faut renvoyer la liste des recettes
     * @return la liste des Recipes pour {@code day}
     */
    public List<Recipe> getRecipesfor(Day day) {
       return Collections.unmodifiableList(menu.get(day.index));
    }

    /**
     * Ajoute une {@code Recipe} dans la liste des Recette pour le jour {@code day}
     * @param day Le jour dans lequel il faut ajouter {@code meal}
     * @param meal La {@code Recipe} qu'il faut ajouter à la liste des Recettes pour le jour {@code day}
     */
    public void addRecipeTo(Day day, Recipe meal) {
        menu.get(day.index).add(meal);
    }

    public String getName() { return name; }

    /**
     * Ajoute la Recette {@code meal} à l'index {@code index} de la liste de recettes du jour {@code day}
     * @param day:
     */
    public void addRecipeToIndex(int day, int index, Recipe meal){
        menu.get(day).add(index,meal);
    }

    /**
     * Retire la Recette {@code meal} de la liste de recette du jour {@code day}
     * @param day Le jour dans lequel il faut supprimer la recette {@code meal}
     * @param meal La Recette qu'il faut retirer de la liste de recettes de {@code day}
     */
    public void removeRecipeFrom(Day day, Recipe meal) {

        menu.get(day.index).remove(meal);
    }

    /**
     * Remplace une des recette par une autre dans la liste de recettes d'un des jour
     * @param day Le jour identifiant la liste dans laquelle la recette doit être remplacée
     * @param oldMeal L'ancienne recette qui sera remplacée
     * @param newMeal La recette qui remplacera {@code oldMeal}
     */
    public void replaceRecipe(Day day, Recipe oldMeal, Recipe newMeal) {
        final int oldIndex = menu.get(day.index).indexOf(oldMeal);
        menu.get(day.index).set(oldIndex, newMeal);
    }

    /**
     * Supprime toutes les recettes présentes dans la liste de recettes pour un jour donné
     * @param day Le jour duquel il faut vider la liste des recettes
     */
    public void clearDay(Day day) {
        menu.get(day.index).clear();
    }

    /**
     * Génère une Liste De Course contenant tout les produits nécessaires pour réaliser les recettes présentes
     * dans le {@code Menu}
     * @return la Liste de Course construite
     * @see ShoppingList
     */
    public ShoppingList generateShoppingList() {
        ShoppingList shopList = new ShoppingList(name);
        for(int i = 0; i < menu.size(); i++){
            for(Recipe meal: menu.get(i)){
                for (Product p : meal) {
                    shopList.add(p);
                }
            }
        }
        return shopList;
    }

    /**
     * Renvoie une liste contenant toutes les recettes présente dans le Menu
     */
    private List<Recipe> getAllRecipes() {

        Vector<Recipe> allRecipesList = new Vector<>();
        for (Vector<Recipe> menuDay : menu) {
            allRecipesList.addAll(menuDay);
        }
        return allRecipesList;
    }

    /**
     * Complète automatiquement le {@code Menu} en fonction des paramètres fournis
     * @param db Un objet {@code Database} permettant d'obtenir la liste de produits correspondant à certains filtres
     * @param nbVegetarian Nombre de repas Végétarien à insérer au minimum
     * @param nbMeat Nombre de repas contenant de la viande à insérer au minimum
     * @param nbFish Nombre de repas contenant du poisson à insérer au minimum
     */
    public void generateMenu(Database db, int nbVegetarian, int nbMeat, int nbFish) throws SQLException {

        HashMap<String, Integer> categoriesWanted = new HashMap<>();
        List<Recipe> recipesUsed = getAllRecipes();

        if (nbVegetarian > 0) {categoriesWanted.put("Végétarien", nbVegetarian);}
        if (nbMeat > 0) {categoriesWanted.put("Viande", nbMeat);}
        if (nbFish > 0) {categoriesWanted.put("Poisson", nbFish);}
        if (categoriesWanted.isEmpty()) { return;}

        int index = 0;
        int nbMealDay = (int) Math.ceil((double)(nbVegetarian + nbMeat + nbFish + recipesUsed.size())/ 7);

        for (Vector<Recipe> nbMeal : menu) {
            int nbRecipesToAdd = nbMealDay - nbMeal.size();
            if (nbRecipesToAdd > 0) {
                List<Recipe> recipesChosed = AutoCompletion.generateRecipesList(recipesUsed, categoriesWanted, nbRecipesToAdd,  null,  db);
                menu.get(index).addAll(recipesChosed);
            }
            index++;
        }
    }

    @Override
    public String toString(){
        return this.name;
    }
}