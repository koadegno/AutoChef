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

        menu = new Vector<>(10);
        for (int i = 0; i<nbOfdays; i++) {
            menu.add(new Vector<>());
        }
    }

    public List<Recipe> getMealsfor(Day day) {
       return Collections.unmodifiableList(menu.get(day.index));
    }

    public int getNbOfdays() { return nbOfdays; }

    public String getName() { return name; }

    public int size() {
        int size = 0;
        for(Vector<Recipe> vector : menu){
            size += vector.size();
        }
        return size;
    }
    public void addMealTo(Day day, Recipe meal) {
        menu.get(day.index).add(meal);
    }

    public void addMealToIndex(int day, int index, Recipe meal){
        menu.get(day).add(index,meal);
    }

    public void removeMealFrom(Day day, Recipe meal) {
        menu.get(day.index).remove(meal);
    }

    public void replaceMeal(Day day, Recipe oldMeal, Recipe newMeal) {
        final int oldIndex = menu.get(day.index).indexOf(oldMeal);
        menu.get(day.index).set(oldIndex, newMeal);
    }

    public void clearDay(Day day) {
        menu.get(day.index).clear();
    }

    public ShoppingList generateShoppingList() {
        ShoppingList shopList = new ShoppingList(name);
        for (Vector<Recipe> menuDay : menu) {
            for (Recipe meal : menuDay)
                for (Product p : meal) {
                    shopList.add(p);
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

        if (nbVegetarian > 0) {categoriesWanted.put("Végétarien", nbVegetarian);}
        if (nbMeat > 0) {categoriesWanted.put("Viande", nbMeat);}
        if (nbFish > 0) {categoriesWanted.put("Poisson", nbFish);}

        int index = 0;
        int nbMealDay = 2;

        List<Recipe> recipesUsed = getAllRecipes();

        for (Vector<Recipe> nbMeal : menu) {
            int nbRecipesToAdd = nbMealDay - nbMeal.size();
            if (nbRecipesToAdd > 0) {
                List<Recipe> recipesChosed = AutoCompletion.generateMenu(recipesUsed, categoriesWanted, nbRecipesToAdd,  null,  db);
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