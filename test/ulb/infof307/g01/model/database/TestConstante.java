package ulb.infof307.g01.model.database;

import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;

public class TestConstante {
    public static final String FOOD_CATEGORY_MEAT = "Viande";
    public static final String FOOD_CATEGORY_FISH = "Poisson";
    public static final String FOOD_CATEGORY_VEGE = "Végétarien";
    public static final String FOOD_CATEGORY_VEGAN = "Vegan";

    public static final String FOOD_TYPE_SIMMERED = "Mijoté";
    public static final String FOOD_TYPE_MEAL = "Plat";
    public static final String FOOD_TYPE_DESSERT = "Dessert";

    public static final String FRUIT = "Fruit";
    public static final String GRAM = "g";

    static final int DURATION = 60;

    public static final Recipe BOLO_RECIPE = new Recipe.RecipeBuilder().withName("Bolognaise").withDuration(DURATION).withCategory(FOOD_CATEGORY_MEAT).withType(FOOD_TYPE_SIMMERED).withNumberOfPerson(4).withPreparation( "Cuire des pâtes, oignons, tomates, ail, basilic").build();
    public static final Recipe CARBO_RECIPE = new Recipe.RecipeBuilder().withName("Carbonara").withDuration(DURATION).withCategory(FOOD_CATEGORY_FISH).withType(FOOD_TYPE_MEAL).withNumberOfPerson(5).withPreparation(  "Cuire des pâtes, poisson").build();
    public static final Recipe PESTO_RECIPE = new Recipe.RecipeBuilder().withName("Pesto").withDuration(20).withCategory(FOOD_CATEGORY_FISH).withType(FOOD_TYPE_MEAL).withNumberOfPerson(3).withPreparation(  "Cuire des pâtes, poisson").build();
    public static final Recipe TIRAMISU_RECIPE = new Recipe.RecipeBuilder().withName("Tiramisu").withDuration(20).withCategory("Végétarien").withType("Dessert").withNumberOfPerson(3).withPreparation("Preparer la mascarpone").build();

    public static final String FAMILY_PRODUCT_MEAT = "Viande";
    public static final String FAMILY_PRODUCT_FISH = "Poison";

    static final Menu MENU_TEST_1 = new Menu("Menu Test").
            addRecipeTo(Day.Monday, BOLO_RECIPE).addRecipeTo(Day.Wednesday, CARBO_RECIPE).
            addRecipeTo(Day.Monday,BOLO_RECIPE).addRecipeTo(Day.Friday, PESTO_RECIPE);

    static final Menu MENU_TEST_2 = new Menu("Menu Test2").
            addRecipeTo(Day.Monday, CARBO_RECIPE).
            addRecipeTo(Day.Wednesday, PESTO_RECIPE).
            addRecipeTo(Day.Monday,PESTO_RECIPE).
            addRecipeTo(Day.Friday, BOLO_RECIPE);

    private TestConstante(){}
}
