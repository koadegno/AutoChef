package ulb.infof307.g01.model.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test du DAO menu
 */
class TestMenuDao {


    @BeforeAll
    static public void initConfig() throws SQLException {
        String databaseName = "test.sqlite";
        Configuration.getCurrent().setDatabase(databaseName);

        User testUser = new User("admin","admin",true);
        testUser.setId(1);
        Configuration.getCurrent().setCurrentUser(testUser);

        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_FISH);
        Configuration.getCurrent().getRecipeCategoryDao().insert(TestConstante.FOOD_CATEGORY_MEAT);

        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_MEAL);
        Configuration.getCurrent().getRecipeTypeDao().insert(TestConstante.FOOD_TYPE_SIMMERED);

        Configuration.getCurrent().getRecipeDao().insert(TestConstante.BOLO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.CARBO_RECIPE);
        Configuration.getCurrent().getRecipeDao().insert(TestConstante.PESTO_RECIPE);

        Configuration.getCurrent().getMenuDao().insert(TestConstante.MENU_TEST_2);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        List<String> menus = Configuration.getCurrent().getMenuDao().getAllName();
        assertEquals("Menu Test2", menus.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        Configuration.getCurrent().getMenuDao().insert(TestConstante.MENU_TEST_1);
        Menu newMenu = Configuration.getCurrent().getMenuDao().get("Menu Test");
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = TestConstante.MENU_TEST_1.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
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
                    assertEquals(nextRecipeFromMenuProduct.getFamillyProduct(), nextRecipeFromNewMenuProduct.getFamillyProduct());
                    assertEquals(nextRecipeFromMenuProduct.getQuantity(), nextRecipeFromNewMenuProduct.getQuantity());
                }
            }
        }
    }

    @Test
    void testUpdate() throws SQLException {
        Menu menu2 = TestConstante.MENU_TEST_2;
        menu2.removeRecipeFrom(Day.Friday,TestConstante.BOLO_RECIPE);
        Configuration.getCurrent().getMenuDao().update(menu2);
        Menu newMenu = Configuration.getCurrent().getMenuDao().get("Menu Test2");
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = menu2.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
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
                    assertEquals(nextRecipeFromMenuProduct.getFamillyProduct(), nextRecipeFromNewMenuProduct.getFamillyProduct());
                    assertEquals(nextRecipeFromMenuProduct.getQuantity(), nextRecipeFromNewMenuProduct.getQuantity());
                }
            }
        }
    }

    @Test
    void testGet() throws SQLException {
        Menu newMenu = Configuration.getCurrent().getMenuDao().get("Menu Test2");
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = TestConstante.MENU_TEST_2.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
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
                    assertEquals(nextRecipeFromMenuProduct.getFamillyProduct(), nextRecipeFromNewMenuProduct.getFamillyProduct());
                    assertEquals(nextRecipeFromMenuProduct.getQuantity(), nextRecipeFromNewMenuProduct.getQuantity());
                }
            }
        }
    }
}