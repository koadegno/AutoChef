package ulb.infof307.g01.model.database;

import ulb.infof307.g01.model.*;
import ulb.infof307.g01.model.database.dao.*;

import java.sql.SQLException;

/**
 * Singleton permettant l'utilisation d'une base de données unique
 * et d'un utilisateur unique
 */
public class Configuration {
    private static Configuration current;
    private static User currentUser;

    private Dao<Menu> menuDao;
    private Dao<Product> productDao;
    private Dao<String> productFamilyDao;
    private Dao<String> productUnityDao;
    private Dao<String> recipeCategoryDao;
    private Dao<Recipe> recipeDao;
    private Dao<String> recipeTypeDao;
    private Dao<ShoppingList> shoppingListDao;
    private Dao<Shop> shopDao;
    private Dao<String> mailAddressDao;
    private Dao<User> userDao;



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
        this.shopDao = new ShopDao(dbPath);
        this.mailAddressDao = new MailAddressDao(dbPath);
        this.userDao = new UserDao(dbPath);
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
        getShopDao().closeConnection();
        getMailAddressDao().closeConnection();
        getUserDao().closeConnection();
    }


    public void setCurrentUser(User user){currentUser = user;}

    public User getCurrentUser(){return currentUser;}

    public MenuDao getMenuDao(){
        return (MenuDao) menuDao;
    }

    public ProductDao getProductDao() { return (ProductDao) productDao; }

    public MailAddressDao getMailAddressDao() {
        return (MailAddressDao) mailAddressDao;
    }

    public ProductFamilyDao getProductFamilyDao() { return (ProductFamilyDao) productFamilyDao; }

    public ProductUnityDao getProductUnityDao() { return (ProductUnityDao) productUnityDao; }

    public RecipeCategoryDao getRecipeCategoryDao() { return (RecipeCategoryDao) recipeCategoryDao; }

    public RecipeDao getRecipeDao() { return (RecipeDao) recipeDao; }

    public RecipeTypeDao getRecipeTypeDao() { return (RecipeTypeDao) recipeTypeDao; }

    public ShoppingListDao getShoppingListDao() { return (ShoppingListDao) shoppingListDao; }

    public ShopDao getShopDao() {
        return (ShopDao) shopDao;
    }

    public UserDao getUserDao() { return (UserDao) userDao; }
}
