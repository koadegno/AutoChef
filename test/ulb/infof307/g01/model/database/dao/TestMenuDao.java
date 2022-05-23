package ulb.infof307.g01.model.database.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.TestConstante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test du DAO menu
 */
class TestMenuDao {

    private static final Configuration configuration = Configuration.getCurrent();
    private static MenuDao menuDao;

    @BeforeAll
    static public void initConfig() throws SQLException {
        TestConstante.createDefaultDB();
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.BOLO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.CARBO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.PESTO_RECIPE);
        Configuration.getCurrent().getMenuDao().insert(TestConstante.MENU_TEST_2);
        menuDao = configuration.getMenuDao();
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        configuration.closeConnection();
        Files.deleteIfExists(Path.of(TestConstante.databaseName));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> menus = menuDao.getAllName();
        assertEquals(TestConstante.menuTest2Name, menus.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        menuDao.insert(TestConstante.MENU_TEST_1);
        Menu newMenu = menuDao.get(TestConstante.menuTest1Name);
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = TestConstante.MENU_TEST_1.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
                assertEquals(recipeFromMenu.get(i), recipeFromNewMenu.get(i));
            }
        }
    }

    @Test
    void testUpdate() throws SQLException {
        Menu menu2 = TestConstante.MENU_TEST_2;
        menu2.removeRecipeFrom(Day.Friday,TestConstante.BOLO_RECIPE);
        menuDao.update(menu2);
        Menu newMenu = menuDao.get(TestConstante.menuTest2Name);
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = menu2.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
                assertEquals(recipeFromMenu.get(i), recipeFromNewMenu.get(i));
                /*
                assertEquals(recipeFromMenu.get(i).getName(),recipeFromNewMenu.get(i).getName());
                assertEquals(recipeFromMenu.get(i).getDuration(),recipeFromNewMenu.get(i).getDuration());
                assertEquals(recipeFromMenu.get(i).getNbrPerson(),recipeFromNewMenu.get(i).getNbrPerson());
                assertEquals(recipeFromMenu.get(i).getPreparation(),recipeFromNewMenu.get(i).getPreparation());
                assertEquals(recipeFromMenu.get(i).getType(),recipeFromNewMenu.get(i).getType());
                assertEquals(recipeFromMenu.get(i).getCategory(),recipeFromNewMenu.get(i).getCategory());

                Iterator<Product> iteratorRecipeFromMenu = recipeFromMenu.get(i).iterator();
                Iterator<Product> iteratorRecipeFromNewMenu = recipeFromNewMenu.get(i).iterator();

                while (iteratorRecipeFromMenu.hasNext() && iteratorRecipeFromNewMenu.hasNext()) {
                    Product nextRecipeFromMenuProduct    = iteratorRecipeFromMenu.next();
                    Product nextRecipeFromNewMenuProduct = iteratorRecipeFromNewMenu.next();

                    assertEquals(nextRecipeFromMenuProduct.getName(), nextRecipeFromNewMenuProduct.getName());
                    assertEquals(nextRecipeFromMenuProduct.getNameUnity(), nextRecipeFromNewMenuProduct.getNameUnity());
                    assertEquals(nextRecipeFromMenuProduct.getFamilyProduct(), nextRecipeFromNewMenuProduct.getFamilyProduct());
                    assertEquals(nextRecipeFromMenuProduct.getQuantity(), nextRecipeFromNewMenuProduct.getQuantity());
                }

                 */
            }
        }
    }

    @Test
    void testGet() throws SQLException {
        Menu newMenu = menuDao.get(TestConstante.menuTest2Name);
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = TestConstante.MENU_TEST_2.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
                assertEquals(recipeFromMenu.get(i), recipeFromNewMenu.get(i));
                /*
                assertEquals(recipeFromMenu.get(i).getName(),recipeFromNewMenu.get(i).getName());
                assertEquals(recipeFromMenu.get(i).getDuration(),recipeFromNewMenu.get(i).getDuration());
                assertEquals(recipeFromMenu.get(i).getNbrPerson(),recipeFromNewMenu.get(i).getNbrPerson());
                assertEquals(recipeFromMenu.get(i).getPreparation(),recipeFromNewMenu.get(i).getPreparation());
                assertEquals(recipeFromMenu.get(i).getType(),recipeFromNewMenu.get(i).getType());
                assertEquals(recipeFromMenu.get(i).getCategory(),recipeFromNewMenu.get(i).getCategory());

                Iterator<Product> iteratorRecipeFromMenu = recipeFromMenu.get(i).iterator();
                Iterator<Product> iteratorRecipeFromNewMenu = recipeFromNewMenu.get(i).iterator();
                while (iteratorRecipeFromMenu.hasNext() && iteratorRecipeFromNewMenu.hasNext()) {
                    Product nextRecipeFromMenuProduct    = iteratorRecipeFromMenu.next();
                    Product nextRecipeFromNewMenuProduct = iteratorRecipeFromNewMenu.next();

                    assertEquals(nextRecipeFromMenuProduct.getName(), nextRecipeFromNewMenuProduct.getName());
                    assertEquals(nextRecipeFromMenuProduct.getNameUnity(), nextRecipeFromNewMenuProduct.getNameUnity());
                    assertEquals(nextRecipeFromMenuProduct.getFamilyProduct(), nextRecipeFromNewMenuProduct.getFamilyProduct());
                    assertEquals(nextRecipeFromMenuProduct.getQuantity(), nextRecipeFromNewMenuProduct.getQuantity());
                }

                 */
            }
        }
    }
}