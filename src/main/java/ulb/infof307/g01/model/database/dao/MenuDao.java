package ulb.infof307.g01.model.database.dao;

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
                int recipeID = getIDFromName("Recette", recipeOfDay.get(hour).getName(), "RecetteID");
                String query = String.format("""
                    INSERT INTO %s values (%s,%s,%s,%s);
                """,MENU_RECIPE_TABLE_NAME, menuID,day.ordinal(),hour,recipeID);
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    sendQueryUpdate(statement);
                }
            }
        }
    }


    private Menu fillMenuWithRecipes(String nameMenu) throws SQLException {
        int nameID = getIDFromName(MENU_TABLE_NAME,nameMenu,"MenuID");
        ResultSet querySelectMenu = sendQuery(String.format("""
                SELECT M.Jour,M.Heure,R.RecetteID, R.Nom, R.Duree, R.NbPersonnes, R.Preparation, Categorie.Nom, TypePlat.Nom
                FROM MenuRecette as M
                INNER JOIN Recette as R ON M.RecetteID = R.RecetteID\s
                INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID
                INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID
                INNER JOIN UtilisateurMenu ON M.MenuID = UtilisateurMenu.MenuID
                WHERE M.MenuID = %d AND UtilisateurMenu.UtilisateurID = %d
                 order by M.Heure""", nameID, Configuration.getCurrent().getCurrentUser().getId()));
        Menu menu = new Menu(nameMenu);
        while(querySelectMenu.next()){
            int menuDay = querySelectMenu.getInt(1);
            int recipeID = querySelectMenu.getInt(3);
            String recipeName = querySelectMenu.getString(4);
            int recipeDuration = querySelectMenu.getInt(5);
            int recipeNumberPersons = querySelectMenu.getInt(6);
            String recipePreparation = querySelectMenu.getString(7);
            String categoryName = querySelectMenu.getString(8);
            String typeName = querySelectMenu.getString(9);
            Recipe recipe = new Recipe.RecipeBuilder().withId(recipeID).withName(recipeName).withDuration(recipeDuration).withCategory(categoryName).withType(typeName).withNumberOfPerson(recipeNumberPersons).withPreparation(recipePreparation).build();
            menu.addRecipeTo(menuDay,recipe);
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
        insertMenu(menu);
        int menuID = getGeneratedID();
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

    private void insertMenu(Menu menu) throws SQLException {
        int nameIndexInPrepared = 1;
        String query = String.format("""
            INSERT INTO %s values (null, ?,%s);
            """,MENU_TABLE_NAME, Menu.NB_OF_DAYS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(nameIndexInPrepared, menu.getName()); //le nom est entrée par l utilisateur
            sendQueryUpdate(statement);
        }
    }

    /**
     * Sauvegarde un menu modifié, et le supprime si il ne contient rien
     */
    @Override
    public void update(Menu menu) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        int menuID = getIDFromName(MENU_TABLE_NAME,menu.getName(),"MenuID");
        constraint.add(String.format("%s = %d","MenuID",menuID));
        delete("MenuRecette",constraint);
        delete("UtilisateurMenu",constraint);
        updateName(MENU_TABLE_NAME, menu.getName(),constraint);
        if(menu.size() == 0){
            delete(MENU_TABLE_NAME,constraint);
        }
        else {
            insertRecipesInMenu(menu, menuID);
            insertUserMenu(menuID);
        }
    }

    @Override
    public Menu get(String name) throws SQLException {
        Menu menu = fillMenuWithRecipes(name);
        for(Day day : Day.values()){
            List<Recipe> recipesFromMenu = menu.getRecipesfor(day);
            for(Recipe recipe : recipesFromMenu){
                fillRecipeWithProducts(recipe);
            }
        }
        menu.setName(name);
        return menu;
    }

}
