import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.AutoCompletion;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.db.Database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAutoCompletion {

    @Test
    public void TestGenerateMenu() {

        Database db = new Database("test.sqlite");
        db.insertCategory("Poisson");
        db.insertCategory("Viande");

        db.insertType("Plat");
        db.insertType("Mijoté");
        Recipe bolo  = new Recipe("Bolognaise", 60, "Viande", "Mijoté",4, "Cuire des pâtes, oignons, tomates, ail, basilic");
        Recipe carbo = new Recipe("Carbonara", 60, "Poisson", "Plat",5, "Cuire des pâtes, poisson");
        Recipe pesto = new Recipe("Pesto", 20, "Poisson", "Plat",3, "Cuire des pâtes, poisson");

        db.insertRecipe(bolo);
        db.insertRecipe(carbo);
        db.insertRecipe(pesto);

        ArrayList<Recipe> recipesAllReadyUsed = new ArrayList<>();
        List<Recipe> recipes = AutoCompletion.generateMenu(recipesAllReadyUsed,4, db);
        System.out.println(recipes);
    }

    @Test
    void TestFindMin() {

        HashMap<String, Integer> recipes = new HashMap<>();
        recipes.put("Poisson", 1);
        recipes.put("Viande", 10);
        recipes.put("Vegetarien", 3);
        recipes.put("Poulet", 7);

        assertEquals("Poisson",AutoCompletion.findMin(recipes));
    }

    @Test
    public void TestChoiceRecipe() {

        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Recipe> recipesAllReadyUsed = new ArrayList<>();

        Recipe bolognaise = new Recipe(1, "Bolognaise", 45, "Viande",
                "Plat principal", 1, "Faire le test avant le code");
        Recipe bolognaiseVegan = new Recipe(2, "Bolognaise", 45, "Vegan",
                "Plat principal", 1, "Faire le test avant le code");
        Recipe soupePotiron = new Recipe(3, "Soupe potiron", 15, "Vegan",
                "Soupe", 2, "Faire le test avant le code");
        Recipe margherita = new Recipe(4, "Margherita", 45, "Vegan",
                "Plat principal", 1, "Faire le test avant le code");
        Recipe lasagne = new Recipe(5, "lasagne", 45, "Viande",
                "Plat principal", 1, "Faire le test avant le code");

        recipes.add(bolognaise);
        recipes.add(bolognaiseVegan);
        recipes.add(soupePotiron);
        recipes.add(margherita);
        recipes.add(lasagne);

        recipesAllReadyUsed.add(bolognaise);
        recipesAllReadyUsed.add(bolognaiseVegan);
        recipesAllReadyUsed.add(soupePotiron);
        recipesAllReadyUsed.add(margherita);

        assertEquals(lasagne, AutoCompletion.choiceRecipe(recipes, recipesAllReadyUsed));
    }
}