package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test du DAO recette
 */
class TestRecipeDao {

    static private final Configuration configuration = Configuration.getCurrent();
    static private RecipeDao recipeDao;
    static private final Product peach = TestConstante.PEACH;
    static private final Product strawberry = TestConstante.STRAWBERRY;
    static private final Recipe bolo = TestConstante.BOLO_RECIPE;
    static private final Recipe carbo = TestConstante.CARBO_RECIPE;
    static private final Recipe pesto = TestConstante.PESTO_RECIPE;

    @BeforeAll
    static public void initConfig() throws SQLException {
        TestConstante.createDefaultDB();
        recipeDao = configuration.getRecipeDao();
        configuration.getProductDao().insert(peach);
        configuration.getProductDao().insert(strawberry);
        bolo.add(peach);
        carbo.add(strawberry);
        pesto.add(strawberry);

        recipeDao.insert(bolo);
        recipeDao.insert(pesto);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    public void testGetRecipeWhereCategory() throws SQLException {
        List<Recipe> recipes = recipeDao.getRecipeWhere(TestConstante.FOOD_CATEGORY_MEAT,null,0);
        assertEquals(bolo, recipes.get(0));
        /*
        assertEquals(bolo.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
        assertEquals(recipes.get(0), bolo);*/
    }

    @Test
    public void testGetRecipeWhereType() throws SQLException {
        List<Recipe> recipes = recipeDao.getRecipeWhere(null, TestConstante.FOOD_TYPE_MEAL,0);
        assertEquals(pesto, recipes.get(0));
        /*
        assertEquals(pesto.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(pesto.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(pesto.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(pesto.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(pesto.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(pesto.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
        assertEquals(recipes.get(0), pesto);*/
    }

    @Test
    public void testGetRecipeWhereNbPerson() throws SQLException {
        List<Recipe> recipes = recipeDao.getRecipeWhere(null,null,3);
        assertEquals(pesto, recipes.get(0));
        /*
        assertEquals(pesto.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(pesto.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(pesto.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(pesto.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(pesto.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(pesto.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
        assertEquals(recipes.get(0), pesto);*/
    }

    @Test
    public void testGetRecipeWhereAll() throws SQLException {
        List<Recipe> recipes = recipeDao.getRecipeWhere(TestConstante.FOOD_CATEGORY_MEAT, TestConstante.FOOD_TYPE_SIMMERED,4);
        assertEquals(bolo, recipes.get(0));
        /*
        assertEquals(bolo.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
        assertEquals(recipes.get(0), bolo);*/
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> recipes = recipeDao.getAllName();
        //assertEquals(bolo.getName(), recipes.get(0));
        assertTrue(recipes.contains(bolo.getName()));
        assertTrue(recipes.contains(pesto.getName()));
    }

    @Test
    void testInsert() throws SQLException {
        recipeDao.insert(carbo);
        Recipe carbo2 = recipeDao.get(carbo.getName());
        assertEquals(carbo, carbo2);
        /*
        assertEquals(carbo.getName(), carbo2.getName(),"Test nom de cette recette");
        assertEquals(carbo.getDuration(), carbo2.getDuration(),"Test la duree de la preparation");
        assertEquals(carbo.getCategory(), carbo2.getCategory(),"Test categorie de la recette");
        assertEquals(carbo.getType(), carbo2.getType(),"Test Type de la recette");
        assertEquals(carbo.getNbrPerson(), carbo2.getNbrPerson(),"test le nombre de personne");
        assertEquals(carbo.getPreparation(), carbo2.getPreparation(),"Test la preparation");

         */
    }

    @Test
    void testUpdate() throws SQLException {
        Recipe myPesto = new Recipe.RecipeBuilder().withName("Pesto maison").withDuration(20).withCategory(TestConstante.FOOD_CATEGORY_FISH).withType(TestConstante.FOOD_TYPE_MEAL).withNumberOfPerson(3).withPreparation("Faire cuire des cheveux").build();
        recipeDao.insert(myPesto);
        myPesto = recipeDao.get(myPesto.getName());
        Recipe newMyPesto = new Recipe.RecipeBuilder().withId(myPesto.getId()).withName("Pesto maison").withDuration(25).withCategory(TestConstante.FOOD_CATEGORY_FISH).withType(TestConstante.FOOD_TYPE_MEAL).withNumberOfPerson(2).withPreparation("Faire cuire des cheveux et des pieds poilus ").build();
        recipeDao.update(newMyPesto);
        Recipe pestoUpdated = recipeDao.get(myPesto.getName());

        assertEquals(newMyPesto, pestoUpdated);
        /*
        assertEquals(newMyPesto.getName(), pestoUpdated.getName(),"Test nom de cette recette");
        assertEquals(newMyPesto.getDuration(), pestoUpdated.getDuration(),"Test la duree de la preparation");
        assertEquals(newMyPesto.getCategory(), pestoUpdated.getCategory(),"Test categorie de la recette");
        assertEquals(newMyPesto.getType(), pestoUpdated.getType(),"Test Type de la recette");
        assertEquals(newMyPesto.getNbrPerson(), pestoUpdated.getNbrPerson(),"test le nombre de personne");
        assertEquals(newMyPesto.getPreparation(), pestoUpdated.getPreparation(),"Test la preparation");

        assertNotEquals(newMyPesto.getDuration(), myPesto.getDuration(),"Test la duree de la preparation");
        assertNotEquals(newMyPesto.getNbrPerson(), myPesto.getNbrPerson(),"test le nombre de personne");
        assertNotEquals(newMyPesto.getPreparation(), myPesto.getPreparation(),"Test la preparation");

         */
    }

    @Test
    void testGet() throws SQLException {
        Recipe bolo2 = recipeDao.get(bolo.getName());
        assertEquals(bolo, bolo2);
    }

    @Test
    void testDelete() throws SQLException {
        Recipe tomateCrevette = new Recipe.RecipeBuilder().withName("tomate aux crevettes grises").withDuration(20).withCategory(TestConstante.FOOD_CATEGORY_FISH).withType(TestConstante.FOOD_TYPE_MEAL).withNumberOfPerson(3).withPreparation( "Mange un steak frite c'est mieux").build();
        recipeDao.insert(tomateCrevette);
        tomateCrevette = recipeDao.get(tomateCrevette.getName());
        recipeDao.delete(tomateCrevette);
        Recipe tomateCrevetteInserted = recipeDao.get(tomateCrevette.getName());
        assertNull(tomateCrevetteInserted);
    }
}