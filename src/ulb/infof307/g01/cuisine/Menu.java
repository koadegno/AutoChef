package ulb.infof307.g01.cuisine;

import java.util.List;
import java.util.Vector;
import java.util.Collections;

public class Menu {
    private String name;
    private Vector<Vector<Recipe>> menu;
    private final int nbOfdays = 7;

    Menu(String name) {
        this.name = name;
        menu = new Vector<>(nbOfdays);
    }

    public List<Recipe> getMealsfor(Day day) {
        return Collections.unmodifiableList(menu.get(day.index));
    }

    public void addMealTo(Day day, Recipe meal) {
        menu.get(day.index).add(meal);
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
                shopList.addAll(meal);
        }
        return shopList;
    }

}

