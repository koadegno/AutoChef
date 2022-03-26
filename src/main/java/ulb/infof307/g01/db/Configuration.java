package ulb.infof307.g01.db;

import ulb.infof307.g01.cuisine.Menu;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.cuisine.ShoppingList;

import java.sql.SQLException;

public class Configuration {
    private static Configuration current;

    private Dao<Menu> menuDao;
    private Dao<Product> productDao;
    private Dao<String> productFamilyDao;
    private Dao<String> productUnityDao;
    private Dao<String> recipeCategoryDao;
    private Dao<Recipe> recipeDao;
    private Dao<String> recipeTypeDao;
    private Dao<ShoppingList> shoppingListDao;

    private Configuration(){}

    public static Configuration getCurrent(){
        if(current == null){
            current = new Configuration();
        }
        return current;
    }

    public void setDatabase(String dbPath){
        this.menuDao = new MenuDao(dbPath);
        this.productDao = new ProductDao(dbPath);
        this.productFamilyDao = new ProductFamilyDao(dbPath);
        this.productUnityDao = new ProductUnityDao(dbPath);
        this.recipeCategoryDao = new RecipeCategoryDao(dbPath);
        this.recipeDao = new RecipeDao(dbPath);
        this.recipeTypeDao = new RecipeTypeDao(dbPath);
        this.shoppingListDao = new ShoppingListDao(dbPath);
    }

    public void closeConnection() throws SQLException {
        getMenuDao().closeConnection();
        getProductDao().closeConnection();
        getProductFamilyDao().closeConnection();
        getProductUnityDao().closeConnection();
        getRecipeCategoryDao().closeConnection();
        getRecipeDao().closeConnection();
        getRecipeTypeDao().closeConnection();
        getShoppingListDao().closeConnection();
    }

    public MenuDao getMenuDao(){
        return (MenuDao) menuDao;
    }

    public ProductDao getProductDao() { return (ProductDao) productDao; }

    public ProductFamilyDao getProductFamilyDao() { return (ProductFamilyDao) productFamilyDao; }

    public ProductUnityDao getProductUnityDao() { return (ProductUnityDao) productUnityDao; }

    public RecipeCategoryDao getRecipeCategoryDao() { return (RecipeCategoryDao) recipeCategoryDao; }

    public RecipeDao getRecipeDao() { return (RecipeDao) recipeDao; }

    public RecipeTypeDao getRecipeTypeDao() { return (RecipeTypeDao) recipeTypeDao; }

    public ShoppingListDao getShoppingListDao() { return (ShoppingListDao) shoppingListDao; }
}
