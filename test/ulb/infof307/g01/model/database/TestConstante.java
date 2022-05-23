package ulb.infof307.g01.model.database;

import ulb.infof307.g01.model.*;

import java.sql.SQLException;

/**
 * Constantes utilisées pour les tests
 */
public class TestConstante {
    //------------Catégories Recettes
    public static final String FOOD_CATEGORY_MEAT = "Viande";
    public static final String FOOD_CATEGORY_FISH = "Poisson";
    public static final String FOOD_CATEGORY_VEGE = "Végétarien";
    public static final String FOOD_CATEGORY_VEGAN = "Vegan";



    //-----------Types Recettes
    public static final String FOOD_TYPE_SIMMERED = "Mijoté";
    public static final String FOOD_TYPE_MEAL = "Plat";
    public static final String FOOD_TYPE_DESSERT = "Dessert";


    //------------Unité Produits
    public static final String GRAM = "g";
    public  static final String LITRE = "Litres";


    //-------------Famille des Produits
    public static final String FAMILY_PRODUCT_MEAT = "Viande";
    public static final String FAMILY_PRODUCT_FISH = "Poison";
    public static final String FAMILY_PRODUCT_FRUIT = "Fruit";

    //-------------Produits
    static public final Product PEACH = new Product.ProductBuilder().withName("peche").withFamilyProduct(TestConstante.FAMILY_PRODUCT_FRUIT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
    static public final Product STRAWBERRY = new Product.ProductBuilder().withName("fraise").withFamilyProduct(TestConstante.FAMILY_PRODUCT_FRUIT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();

    //------------Duration Recette
    static final int DURATION = 60;

    //-------------Noms des menus
    public static final String menuTest1Name = "Menu Test1";
    public static final String menuTest2Name = "Menu Test2";

    //--------------Recettes
    public static final String BOLOGNAISE_NAME = "Bolognaise";
    public static final String CARBONARA_NAME = "Carbonara";
    public static final String PESTO_NAME = "Pesto";
    public static final String TIRAMISU_NAME = "Tiramisu";
    public static final Recipe BOLO_RECIPE = new Recipe.RecipeBuilder().withName(BOLOGNAISE_NAME).withDuration(DURATION).withCategory(FOOD_CATEGORY_MEAT).withType(FOOD_TYPE_SIMMERED).withNumberOfPerson(4).withPreparation( "Cuire des pâtes, oignons, tomates, ail, basilic").build();
    public static final Recipe CARBO_RECIPE = new Recipe.RecipeBuilder().withName(CARBONARA_NAME).withDuration(DURATION).withCategory(FOOD_CATEGORY_FISH).withType(FOOD_TYPE_MEAL).withNumberOfPerson(5).withPreparation(  "Cuire des pâtes, poisson").build();
    public static final Recipe PESTO_RECIPE = new Recipe.RecipeBuilder().withName(PESTO_NAME).withDuration(20).withCategory(FOOD_CATEGORY_FISH).withType(FOOD_TYPE_MEAL).withNumberOfPerson(3).withPreparation(  "Cuire des pâtes, poisson").build();
    public static final Recipe TIRAMISU_RECIPE = new Recipe.RecipeBuilder().withName(TIRAMISU_NAME).withDuration(20).withCategory("Végétarien").withType("Dessert").withNumberOfPerson(3).withPreparation("Preparer la mascarpone").build();


    //--------------Menus
    public static final Menu MENU_TEST_1 = new Menu(menuTest1Name).
            addRecipeTo(Day.Monday, BOLO_RECIPE).addRecipeTo(Day.Wednesday, CARBO_RECIPE).
            addRecipeTo(Day.Monday,BOLO_RECIPE).addRecipeTo(Day.Friday, PESTO_RECIPE);

    public static final Menu MENU_TEST_2 = new Menu(menuTest2Name).
            addRecipeTo(Day.Monday, CARBO_RECIPE).
            addRecipeTo(Day.Wednesday, PESTO_RECIPE).
            addRecipeTo(Day.Monday,PESTO_RECIPE).
            addRecipeTo(Day.Friday, BOLO_RECIPE);


    private TestConstante(){}


    //-----------Base de données
    public static final String databaseName = "test.sqlite";
    public static final Configuration configuration = Configuration.getCurrent();
    public static void createDefaultDB() throws SQLException {
        configuration.setDatabase(databaseName);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        configuration.setCurrentUser(testUser);

        //Ajout des catégories des recettes dans la DB
        configuration.getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_MEAT);
        configuration.getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_FISH);
        configuration.getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_VEGE);
        configuration.getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_VEGAN);
        //Ajout des types de recettes dans la DB
        configuration.getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_MEAL);
        configuration.getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_SIMMERED);
        configuration.getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_DESSERT);
        //Ajout des familles des produits dans la DB
        configuration.getProductFamilyDao().insert(TestConstante.FAMILY_PRODUCT_MEAT);
        configuration.getProductFamilyDao().insert(TestConstante.FAMILY_PRODUCT_FISH);
        configuration.getProductFamilyDao().insert(TestConstante.FAMILY_PRODUCT_FRUIT);
        //Ajout des unités des produits dans la DB
        configuration.getProductUnityDao().insert(TestConstante.GRAM);

    }
}
