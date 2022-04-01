package ulb.infof307.g01.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestRecipeDao {

    static private String meat = "Viande";
    static private String fish = "Poisson";

    static private String simmered = "Mijoté";
    static private String meal = "Plat";

    static String fruit = "Fruit";
    static String gram = "g";

    static private Product peach = new Product("peche", 1, gram, fruit);
    static private Product strawberry = new Product( "fraise", 1, gram, fruit);

    static private Recipe bolo = new Recipe("Bolognaise", 60, meat, simmered,4, "Cuire des pâtes, oignons, tomates, ail, basilic");
    static private Recipe carbo = new Recipe("Carbonara", 60, fish, meal,5, "Cuire des pâtes, poisson");
    static private Recipe pesto = new Recipe("Pesto", 20, fish, meal,3, "Cuire des pâtes, poisson");

    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        Configuration.getCurrent().getRecipeCategoryDao().insert(fish);
        Configuration.getCurrent().getRecipeCategoryDao().insert(meat);

        Configuration.getCurrent().getRecipeTypeDao().insert(meal);
        Configuration.getCurrent().getRecipeTypeDao().insert(simmered);

        Configuration.getCurrent().getProductUnityDao().insert(gram);
        Configuration.getCurrent().getProductFamilyDao().insert(fruit);

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
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(meat,null,0);
        assertEquals(bolo.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
    }

    @Test
    public void testGetRecipeWhereType() throws SQLException {
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(null, meal,0);
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
        ArrayList<Recipe> recipes = Configuration.getCurrent().getRecipeDao().getRecipeWhere(meat, simmered,4);
        assertEquals(bolo.getName(), recipes.get(0).getName(),"Test nom de cette recette");
        assertEquals(bolo.getDuration(), recipes.get(0).getDuration(),"Test la duree de la preparation");
        assertEquals(bolo.getCategory(), recipes.get(0).getCategory(),"Test categorie de la recette");
        assertEquals(bolo.getType(), recipes.get(0).getType(),"Test Type de la recette");
        assertEquals(bolo.getNbrPerson(), recipes.get(0).getNbrPerson(),"test le nombre de personne");
        assertEquals(bolo.getPreparation(), recipes.get(0).getPreparation(),"Test la preparation");
    }

    @Test
    void testGetAllName() throws SQLException {
        ArrayList<String> recipes = Configuration.getCurrent().getRecipeDao().getAllName();
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
    void testUpdate() {
        System.out.println("A FAIRE ENCORE");
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
}