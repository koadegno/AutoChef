package ulb.infof307.g01.db.dao;

import ulb.infof307.g01.db.Database;
import ulb.infof307.g01.model.Day;
import ulb.infof307.g01.model.Menu;
import ulb.infof307.g01.model.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDao extends Database implements Dao<Menu> {
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
                String[] values = {String.format("%d",menuID),String.format("%d",day.getIndex()),String.format("%d",hour),String.format("%d",recipeID)};
                insert("MenuRecette",values);
            }
        }
    }

    private Menu fillMenuWithRecipes(String nameMenu) throws SQLException {
        int nameID = getIDFromName("Menu",nameMenu,"MenuID");
        ResultSet querySelectMenu = sendQuery(String.format("SELECT M.Jour,M.Heure,R.RecetteID, R.Nom, R.Duree," +
                " R.NbPersonnes, R.Preparation, Categorie.Nom, TypePlat.Nom\n" +
                "FROM MenuRecette as M\n" +
                "INNER JOIN Recette as R ON M.RecetteID = R.RecetteID \n" +
                "INNER JOIN TypePlat ON R.TypePlatID = TypePlat.TypePlatID\n" +
                "INNER JOIN Categorie ON R.CategorieID = Categorie.CategorieID\n" +
                "WHERE M.MenuID = %d order by M.Heure", nameID));
        Menu menu = new Menu(nameMenu);
        while(querySelectMenu.next()){
            int menuDay = querySelectMenu.getInt(1);
            int menuHour = querySelectMenu.getInt(2);
            int recipeID = querySelectMenu.getInt(3);
            String recipeName = querySelectMenu.getString(4);
            int recipeDuration = querySelectMenu.getInt(5);
            int recipeNumberPersons = querySelectMenu.getInt(6);
            String recipePreparation = querySelectMenu.getString(7);
            String categoryName = querySelectMenu.getString(8);
            String typeName = querySelectMenu.getString(9);
            Recipe recipe = new Recipe(recipeID,recipeName,recipeDuration,categoryName,typeName,recipeNumberPersons,recipePreparation);
            menu.addRecipeToIndex(menuDay,menuHour,recipe);
        }
        return menu;
    }

    @Override
    public ArrayList<String> getAllName() throws SQLException {
        return getAllNameFromTable("Menu","ORDER BY Nom ASC");
    }

    @Override
    public void insert(Menu menu) throws SQLException {
        String[] values = {"null",String.format("'%s'",menu.getName()),String.format("%d",menu.getNbOfdays())};
        insert("Menu",values);
        int id = getGeneratedID();
        insertRecipesInMenu(menu, id);
    }

    /**
     * Sauvegarde un menu modifié, et le supprime si il ne contient rien
     */
    @Override
    public void update(Menu menu) throws SQLException {
        ArrayList<String> constraint = new ArrayList<>();
        int menuID = getIDFromName("Menu",menu.getName(),"MenuID");
        constraint.add(String.format("%s = %d","MenuID",menuID));
        delete("MenuRecette",constraint);
        updateName("Menu", menu.getName(),constraint);
        if(menu.size() == 0){
            delete("Menu",constraint);
        }
        else {
            insertRecipesInMenu(menu, menuID);
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
        return menu;
    }

}
