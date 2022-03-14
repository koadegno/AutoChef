package ulb.infof307.g01.cuisine;
import ulb.infof307.g01.db.Database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Collections;

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

    public int size() {
        int size = 0;
        for(Vector<Recipe> vector : menu){
            size += vector.size();
        }
        return size;
    }

    public int getNbOfdays() { return nbOfdays;}

    public List<Recipe> getRecipesfor(Day day) {
       return Collections.unmodifiableList(menu.get(day.index));
    }

    public void addRecipeTo(Day day, Recipe meal) {
        menu.get(day.index).add(meal);
    }

    public String getName() { return name; }


    public void addRecipeToIndex(int day, int index, Recipe meal){
        menu.get(day).add(index,meal);
    }

    public void removeRecipeFrom(Day day, Recipe meal) {
        menu.get(day.index).remove(meal);
    }

    public void replaceRecipe(Day day, Recipe oldMeal, Recipe newMeal) {
        final int oldIndex = menu.get(day.index).indexOf(oldMeal);
        menu.get(day.index).set(oldIndex, newMeal);
    }

    public void clearDay(Day day) {
        menu.get(day.index).clear();
    }

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

    private List<Recipe> getAllRecipes() {

        Vector<Recipe> allRecipesList = new Vector<>();
        for (Vector<Recipe> menuDay : menu) {
            allRecipesList.addAll(menuDay);
        }
        return allRecipesList;
    }

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
                List<Recipe> recipesChosed = AutoCompletion.generaRecipesList(recipesUsed, categoriesWanted, nbRecipesToAdd,  null,  db);
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