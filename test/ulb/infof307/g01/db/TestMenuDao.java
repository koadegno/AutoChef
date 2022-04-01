package ulb.infof307.g01.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.cuisine.Day;
import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Recipe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestMenuDao {

    static private String meat = "Viande";
    static private String fish = "Poisson";
    static private String simmered = "Mijoté";
    static private String meal = "Plat";

    static private Menu menu = new Menu("Menu Test");
    static private Menu menu2 = new Menu("Menu Test2");

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

        Configuration.getCurrent().getRecipeDao().insert(bolo);
        Configuration.getCurrent().getRecipeDao().insert(carbo);
        Configuration.getCurrent().getRecipeDao().insert(pesto);

        menu.addRecipeTo(Day.Monday, bolo);
        menu.addRecipeTo(Day.Wednesday, carbo);
        menu.addRecipeTo(Day.Monday,bolo);
        menu.addRecipeTo(Day.Friday, pesto);

        menu2.addRecipeTo(Day.Monday, carbo);
        menu2.addRecipeTo(Day.Wednesday, pesto);
        menu2.addRecipeTo(Day.Monday,pesto);
        menu2.addRecipeTo(Day.Friday, bolo);

        Configuration.getCurrent().getMenuDao().insert(menu2);
    }

    @AfterAll
    static public void deleteConfig() throws SQLException, IOException {
        Configuration.getCurrent().closeConnection();
        Files.deleteIfExists(Path.of("test.sqlite"));
    }

    @Test
    void testGetAllName() throws SQLException {
        ArrayList<String> menus = Configuration.getCurrent().getMenuDao().getAllName();
        assertEquals("Menu Test2", menus.get(0));
    }

    @Test
    void testInsert() throws SQLException {
        Configuration.getCurrent().getMenuDao().insert(menu);
        Menu newMenu = Configuration.getCurrent().getMenuDao().get("Menu Test");
        for(Day day : Day.values()){
            List<Recipe> recipeFromNewMenu = newMenu.getRecipesfor(day);
            List<Recipe> recipeFromMenu = menu.getRecipesfor(day);
            for(int i = 0; i < recipeFromMenu.size(); i++){
                assertEquals(recipeFromMenu.get(i).getName(),recipeFromNewMenu.get(i).getName());
                assertEquals(recipeFromMenu.get(i).getDuration(),recipeFromNewMenu.get(i).getDuration());
                assertEquals(recipeFromMenu.get(i).getNbrPerson(),recipeFromNewMenu.get(i).getNbrPerson());
                assertEquals(recipeFromMenu.get(i).getPreparation(),recipeFromNewMenu.get(i).getPreparation());
                assertEquals(recipeFromMenu.get(i).getType(),recipeFromNewMenu.get(i).getType());
                assertEquals(recipeFromMenu.get(i).getCategory(),recipeFromNewMenu.get(i).getCategory());

                for (int j = 0; j < recipeFromMenu.get(i).size(); j++) {
                    assertEquals(recipeFromMenu.get(i).get(j).getName(),recipeFromNewMenu.get(i).get(j).getName());
                    assertEquals(recipeFromMenu.get(i).get(j).getNameUnity(),recipeFromNewMenu.get(i).get(j).getNameUnity());
                    assertEquals(recipeFromMenu.get(i).get(j).getFamillyProduct(),recipeFromNewMenu.get(i).get(j).getFamillyProduct());
                    assertEquals(recipeFromMenu.get(i).get(j).getQuantity(),recipeFromNewMenu.get(i).get(j).getQuantity());
                }
            }
        }
    }

    @Test
    void testUpdate() throws SQLException {
        menu2.removeRecipeFrom(Day.Friday,bolo);
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

                for (int j = 0; j < recipeFromMenu.get(i).size(); j++) {
                    assertEquals(recipeFromMenu.get(i).get(j).getName(),recipeFromNewMenu.get(i).get(j).getName());
                    assertEquals(recipeFromMenu.get(i).get(j).getNameUnity(),recipeFromNewMenu.get(i).get(j).getNameUnity());
                    assertEquals(recipeFromMenu.get(i).get(j).getFamillyProduct(),recipeFromNewMenu.get(i).get(j).getFamillyProduct());
                    assertEquals(recipeFromMenu.get(i).get(j).getQuantity(),recipeFromNewMenu.get(i).get(j).getQuantity());
                }
            }
        }
    }

    @Test
    void testGet() throws SQLException {
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

                for (int j = 0; j < recipeFromMenu.get(i).size(); j++) {
                    assertEquals(recipeFromMenu.get(i).get(j).getName(),recipeFromNewMenu.get(i).get(j).getName());
                    assertEquals(recipeFromMenu.get(i).get(j).getNameUnity(),recipeFromNewMenu.get(i).get(j).getNameUnity());
                    assertEquals(recipeFromMenu.get(i).get(j).getFamillyProduct(),recipeFromNewMenu.get(i).get(j).getFamillyProduct());
                    assertEquals(recipeFromMenu.get(i).get(j).getQuantity(),recipeFromNewMenu.get(i).get(j).getQuantity());
                }
            }
        }
    }
}