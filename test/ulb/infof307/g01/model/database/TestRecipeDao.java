package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test du DAO recette
 */
class TestRecipeDao {

    static private final Product peach = new Product.ProductBuilder().withName("peche").withFamilyProduct(TestConstante.FRUIT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
    static private final Product strawberry = new Product.ProductBuilder().withName("fraise").withFamilyProduct(TestConstante.FRUIT).withQuantity(1).withNameUnity(TestConstante.GRAM).build();
    static private final Recipe bolo = TestConstante.BOLO_RECIPE;
    static private final Recipe carbo = TestConstante.CARBO_RECIPE;
    static private final Recipe pesto = TestConstante.PESTO_RECIPE;

    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);
        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_MEAT);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_FISH);

        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_MEAL);
        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_SIMMERED);

        Configuration.getCurrent().getProductUnityDao().insert(TestConstante.GRAM);
        Configuration.getCurrent().getProductFamilyDao().insert(TestConstante.FRUIT);

        Configuration.getCurrent().getProductDao().insert(peach);
        Configuration.getCurrent().getProductDao().insert(strawberry);

        bolo.add(peach);
        carbo.add(strawberry);
        pesto.add(strawberry);

        Configuration.getCurrent().getRecipeDao().insert(bolo);
        Configuration.getCurrent().getRecipeDao().insert(pesto);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    public void testGetRecipeWhereCategory() throws SQLException {
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(TestConstante.FOOD_CATEGORY_MEAT,null,0);
        assertEquals(bolo.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
    }

    @Test
    public void testGetRecipeWhereType() throws SQLException {
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(null, TestConstante.FOOD_TYPE_MEAL,0);
        assertEquals(pesto.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(pesto.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(pesto.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(pesto.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(pesto.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(pesto.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
    }

    @Test
    public void testGetRecipeWhereNbPerson() throws SQLException {
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(null,null,3);
        assertEquals(pesto.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(pesto.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(pesto.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(pesto.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(pesto.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(pesto.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
    }

    @Test
    public void testGetRecipeWhereAll() throws SQLException {
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(TestConstante.FOOD_CATEGORY_MEAT, TestConstante.FOOD_TYPE_SIMMERED,4);
        assertEquals(bolo.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> recipes = Configuration.getCurrent().getRecipeDao().getAllName();
        assertEquals(bolo.getName(), recipes.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        Configuration.getCurrent().getRecipeDao().insert(carbo);
        Recipe carbo2 = Configuration.getCurrent().getRecipeDao().get(carbo.getName());
        assertEquals(carbo.getName(), carbo2.getName(),"Test nom de cette recette");
        assertEquals(carbo.getDuration(), carbo2.getDuration(),"Test la duree de la preparation");
        assertEquals(carbo.getCategory(), carbo2.getCategory(),"Test categorie de la recette");
        assertEquals(carbo.getType(), carbo2.getType(),"Test Type de la recette");
        assertEquals(carbo.getNbrPerson(), carbo2.getNbrPerson(),"test le nombre de personne");
        assertEquals(carbo.getPreparation(), carbo2.getPreparation(),"Test la preparation");
    }

    @Test
    void testUpdate() throws SQLException {
        Recipe myPesto = new Recipe.RecipeBuilder().withName("Pesto maison").withDuration(20).withCategory(TestConstante.FOOD_CATEGORY_FISH).withType(TestConstante.FOOD_TYPE_MEAL).withNumberOfPerson(3).withPreparation("Faire cuire des cheveux").build();
        Configuration.getCurrent().getRecipeDao().insert(myPesto);
        myPesto = Configuration.getCurrent().getRecipeDao().get(myPesto.getName());
        Recipe newMyPesto = new Recipe.RecipeBuilder().withId(myPesto.getId()).withName("Pesto maison").withDuration(25).withCategory(TestConstante.FOOD_CATEGORY_FISH).withType(TestConstante.FOOD_TYPE_MEAL).withNumberOfPerson(2).withPreparation("Faire cuire des cheveux et des pieds poilus ").build();
        Configuration.getCurrent().getRecipeDao().update(newMyPesto);
        Recipe pestoUpdated = Configuration.getCurrent().getRecipeDao().get(myPesto.getName());

        assertEquals(newMyPesto.getName(), pestoUpdated.getName(),"Test nom de cette recette");
        assertEquals(newMyPesto.getDuration(), pestoUpdated.getDuration(),"Test la duree de la preparation");
        assertEquals(newMyPesto.getCategory(), pestoUpdated.getCategory(),"Test categorie de la recette");
        assertEquals(newMyPesto.getType(), pestoUpdated.getType(),"Test Type de la recette");
        assertEquals(newMyPesto.getNbrPerson(), pestoUpdated.getNbrPerson(),"test le nombre de personne");
        assertEquals(newMyPesto.getPreparation(), pestoUpdated.getPreparation(),"Test la preparation");

        assertNotEquals(newMyPesto.getDuration(), myPesto.getDuration(),"Test la duree de la preparation");
        assertNotEquals(newMyPesto.getNbrPerson(), myPesto.getNbrPerson(),"test le nombre de personne");
        assertNotEquals(newMyPesto.getPreparation(), myPesto.getPreparation(),"Test la preparation");
    }

    @Test
    void testGet() throws SQLException {
        Recipe bolo2 = Configuration.getCurrent().getRecipeDao().get(bolo.getName());
        assertEquals(bolo.getName(), bolo2.getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), bolo2.getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), bolo2.getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), bolo2.getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), bolo2.getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), bolo2.getPreparation(),"Test la preparation");
    }

    @Test
    void testDelete() throws SQLException {
        Recipe tomateCrevette = new Recipe.RecipeBuilder().withName("tomate aux crevettes grises").withDuration(20).withCategory(TestConstante.FOOD_CATEGORY_FISH).withType(TestConstante.FOOD_TYPE_MEAL).withNumberOfPerson(3).withPreparation( "Mange un steak frite c'est mieux").build();
        Configuration.getCurrent().getRecipeDao().insert(tomateCrevette);
        tomateCrevette = Configuration.getCurrent().getRecipeDao().get(tomateCrevette.getName());
        Configuration.getCurrent().getRecipeDao().delete(tomateCrevette);
        Recipe tomateCrevetteInserted = Configuration.getCurrent().getRecipeDao().get(tomateCrevette.getName());
        assertNull(tomateCrevetteInserted);


    }
}