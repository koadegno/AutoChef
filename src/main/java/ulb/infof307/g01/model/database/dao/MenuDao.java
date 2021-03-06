package ulb.infof307.g01.model.database.dao;

import org.jetbrains.annotations.NotNull;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.model.database.Database;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès à la base de données pour les données concernant les menus
 */
public class MenuDao extends Database implements Dao<Menu> {

    public static final String MENU_TABLE_NAME = "Menu";
    public static final String  MENU_RECIPE_TABLE_NAME = "MenuRecette";
    public static final String  MENU_USER_TABLE_NAME = "UtilisateurMenu";

    /**
     * Constructeur qui charge une base de données existante si le paramètre nameDB
     * est un fichier de base de données existante. Sinon en créée une nouvelle.
     *
     * @param nameDB nom de la base de données que l'ont veut charger/créer.
     */
    public MenuDao(String nameDB) {
        super(nameDB);
    }

    private void insertRecipesInMenu(Menu menu, int menuID) throws SQLException {
        for(Day day : Day.values()){
            List<Recipe> recipeOfDay = menu.getRecipesfor(day);
            for (int hour = 0; hour < recipeOfDay.size(); hour++) {
                int recipeID = getIDFromName("Recette", recipeOfDay.get(hour).getName());
                String query = String.format("""
                    INSERT INTO %s values (%s,%s,%s,%s);
                """,MENU_RECIPE_TABLE_NAME, menuID,day.ordinal(),hour,recipeID);
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    sendQueryUpdate(statement);
                }
            }
        }
    }

    @NotNull
    private Menu fillMenuWithRecipes(Menu menu, ResultSet resultSet) throws SQLException {
        while(resultSet.next()){
            int menuDay = resultSet.getInt(1);
            int recipeID = resultSet.getInt(3);
            String recipeName = resultSet.getString(4);
            int recipeDuration = resultSet.getInt(5);
            int recipeNumberPersons = resultSet.getInt(6);
            String recipePreparation = resultSet.getString(7);
            String categoryName = resultSet.getString(8);
            String typeName = resultSet.getString(9);
            Recipe recipe = new Recipe.RecipeBuilder().withId(recipeID).withName(recipeName).withDuration(recipeDuration).withCategory(categoryName).withType(typeName).withNumberOfPerson(recipeNumberPersons).withPreparation(recipePreparation).build();
            menu.addRecipeTo(menuDay,recipe);
            fillRecipesWithProduct(menu);
        }
        return menu;
    }

    @Override
    public List<String> getAllName() throws SQLException {
        String query = String.format("""
                SELECT R.Nom
                FROM Menu as R
                INNER JOIN UtilisateurMenu ON R.MenuID = UtilisateurMenu.MenuID
                WHERE UtilisateurMenu.UtilisateurID = %d
                ORDER BY Nom ASC
                """, getUserID());

        return getListOfName(query);
    }

    @Override
    public void insert(Menu menu) throws SQLException {
        int menuID = insertMenu(menu);
        //int menuID = getGeneratedID();
        insertRecipesInMenu(menu, menuID);
        insertUserMenu(menuID);
    }

    private void insertUserMenu(int menuID) throws SQLException {
        String query = String.format("""
            INSERT INTO %s values (%s,%s);
            """,MENU_USER_TABLE_NAME, getUserID(), menuID);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            sendQueryUpdate(statement);
        }
    }

    private int insertMenu(Menu menu) throws SQLException {
        int nameIndexInPrepared = 1;
        String query = String.format("""
            INSERT INTO %s values (null, ?,%s);
            """,MENU_TABLE_NAME, Menu.NB_OF_DAYS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(nameIndexInPrepared, menu.getName()); //le nom est entrée par l utilisateur
            sendQueryUpdate(statement);
            return getGeneratedID(statement);
        }
    }

    /**
     * Sauvegarde un menu modifié, et le supprime si il ne contient rien
     */
    @Override
    public void update(Menu menu) throws SQLException {
        int menuID = getIDFromName(MENU_TABLE_NAME,menu.getName());
        int isEmpty = 0;
        String idColumnName = "MenuID";
        deleteByID(menuID, MENU_RECIPE_TABLE_NAME, idColumnName); //supprimer les recettes du menus
        deleteByID(menuID, MENU_USER_TABLE_NAME, idColumnName);  //supprimer des menus de l'utilisateur
        if(menu.size() == isEmpty) {
            deleteByID(menuID, MENU_TABLE_NAME, idColumnName);   //supprimer des menus
            return;
        }
        String query = String.format("""
                        UPDATE %s SET Nom = ?
                        WHERE MenuID = %s
                """, MENU_TABLE_NAME,menuID);
        sendInsertNameQueryWithPreparedStatement(menu.getName(),query); //update query
        insertRecipesInMenu(menu, menuID);
        insertUserMenu(menuID);

    }



    @Override
    public Menu get(String name) throws SQLException {
        int menuID = getIDFromName(MENU_TABLE_NAME,name);
        String query = String.format("""
                SELECT M.Jour,M.Heure,R.RecetteID, R.Nom, R.Duree, R.NbPersonnes, R.Preparation, Categorie.Nom, TypePlat.Nom
                FROM MenuRecette as M
                INNER JOIN Recette as R ON M.RecetteID = R.RecetteID\s
                INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID
                INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID
                INNER JOIN UtilisateurMenu ON M.MenuID = UtilisateurMenu.MenuID
                WHERE M.MenuID = %d AND UtilisateurMenu.UtilisateurID = %d
                 order by M.Heure""", menuID, getUserID());
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet =  sendQuery(statement);
            Menu menu = new Menu(name);
            return fillMenuWithRecipes(menu, resultSet);
        }
    }

    private void fillRecipesWithProduct(Menu menu) throws SQLException {
        String idColumnName = "RecetteID";
        String recipeProductTableName = "RecetteIngredient";
        for(Day day : Day.values()){
            List<Recipe> recipesFromMenu = menu.getRecipesfor(day);
            for(Recipe recipe : recipesFromMenu){
                fillProductHashset(recipe, recipe.getId(),recipeProductTableName,idColumnName,false);
            }
        }
    }

}
