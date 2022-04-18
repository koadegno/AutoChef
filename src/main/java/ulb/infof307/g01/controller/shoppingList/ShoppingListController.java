package ulb.infof307.g01.controller.shoppingList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sqlite.SQLiteException;
import ulb.infof307.g01.controller.Controller;
import ulb.infof307.g01.controller.HomePageController;
import ulb.infof307.g01.controller.recipe.RecipeController;
import ulb.infof307.g01.controller.mail.MailController;
import ulb.infof307.g01.controller.menu.UserMenusController;
import ulb.infof307.g01.controller.tools.AlertMessageController;
import ulb.infof307.g01.model.Product;
import ulb.infof307.g01.model.ShoppingList;
import ulb.infof307.g01.model.database.Configuration;
import ulb.infof307.g01.view.shoppingList.CreateUserShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.ShoppingListViewController;
import ulb.infof307.g01.view.shoppingList.UserShoppingListViewController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


public class ShoppingListController extends Controller implements ShoppingListViewController.Listener {
    private ShoppingListViewController shoppingListViewController;
    private UserShoppingListViewController userShoppingListViewController;
    private CreateUserShoppingListViewController createUserShoppingListViewController;
    private HomePageController homePageController;
    private ShoppingList shoppingListToSend;

    RecipeController recipeController = null;

    public ShoppingListController(CreateUserShoppingListViewController createUserShoppingListViewController, HomePageController homePageController){
        this.shoppingListViewController = this.createUserShoppingListViewController = createUserShoppingListViewController;
        initShoppingListController(homePageController);
    }

    public ShoppingListController(UserShoppingListViewController userShoppingListViewController, HomePageController homePageController){
        this.shoppingListViewController = this.userShoppingListViewController = userShoppingListViewController;
        initShoppingListController(homePageController);
    }

    public ShoppingListController(CreateUserShoppingListViewController createUserShoppingListViewController){
        this.shoppingListViewController = this.createUserShoppingListViewController = createUserShoppingListViewController;
        this.shoppingListViewController.setListener(this);
    }

    private void initShoppingListController(HomePageController homePageController) {
        this.homePageController = homePageController;
        this.shoppingListViewController.setListener(this);
    }

    public ShoppingListController(RecipeController recipeController) {
        this.recipeController = recipeController;
    }
    //Methode Listener de CreateUserShoppingListViewController

    public void confirmUserCreateShoppingList(String shoppingListName, int sizeTableViewDisplayProductList){
        createUserShoppingListViewController.removeBorderColor();

        if(Objects.equals(shoppingListName, "")){ // champs du nom est vide
            createUserShoppingListViewController.showNameUserCreateShoppingListError();
        }
        else if(sizeTableViewDisplayProductList == 0){ // table view est vide
            createUserShoppingListViewController.showIsEmptyTableViewError();
        }
        else {
            this.shoppingListToSend = new ShoppingList(shoppingListName);
            createUserShoppingListViewController.fillShoppingListToSend();
            try {
                Configuration.getCurrent().getShoppingListDao().insert(shoppingListToSend);
            }
            catch (SQLiteException e) { //Erreur de doublon
                createUserShoppingListViewController.showNameUserCreateShoppingListError();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // else tout ce passe bien
            createUserShoppingListViewController.returnToMenu.fire();
        }
    }

    public void fillProductTable(ShoppingList shoppingList){
        createUserShoppingListViewController.clearProductTableView();
        Vector<Product> temp = new Vector<>(shoppingList);
        final ObservableList<Product> data = FXCollections.observableArrayList(temp);
        createUserShoppingListViewController.setProductTableView(data);
        //Retour menu precedent : MainShoppingList
        createUserShoppingListViewController.setReturnButtonAction();

    }
    //Fin Methode Listener de CreateUserShoppingListViewController

    //Methode Listener de WindowUserShoppingListController

    @Override
    public void seeUserShoppingList(Object nameUserShoppingList){
        if(Objects.equals(nameUserShoppingList, null)){ //nom est null
            userShoppingListViewController.isVisibleElementToModifyMyShoppingList(false);
        }
        else{
            String currentShoppingListName = (String) nameUserShoppingList;
            try {
                // afficher les produits de la liste de course dans la table
                ShoppingList shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
                Vector<Product> productOfShoppingList =  new Vector<>(shoppingList);
                userShoppingListViewController.addProductListToTableView(productOfShoppingList);
                userShoppingListViewController.isVisibleElementToModifyMyShoppingList(true);
                userShoppingListViewController.setCurrentShoppingListName(currentShoppingListName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void confirmUserModifyShoppingList(String currentShoppingListName){
        try {
            //Recupere liste de courses chez la bdd
            ShoppingList shoppingListInDataBase = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
            this.shoppingListToSend = new ShoppingList(shoppingListInDataBase.getName(), shoppingListInDataBase.getId());

            //Renvoie liste de courses chez la bdd
            userShoppingListViewController.fillShoppingListToSend();
            Configuration.getCurrent().getShoppingListDao().update(shoppingListToSend);

            //Popup : confirmer que la liste de courses est enregistrer
            AlertMessageController alertMessageViewController = new AlertMessageController();
            alertMessageViewController.displayAlertMessage();
            alertMessageViewController.createShoppingListAlertMessage();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProductToShoppingListToSend(Product product){
        shoppingListToSend.add(product);
    }

    public void initInformationShoppingList(boolean isCreateUserShoppingListController){
        try {
            ArrayList<String> allProduct = Configuration.getCurrent().getProductDao().getAllName();
            ArrayList<String> allUnitName = Configuration.getCurrent().getProductUnityDao().getAllName();
            String[] unitToRemove = new String[]{"c.à.s", "c.à.c", "p"};
            allUnitName.removeAll(List.of(unitToRemove));
            ArrayList<String> allShoppinListName = Configuration.getCurrent().getShoppingListDao().getAllName();

            if(isCreateUserShoppingListController) createUserShoppingListViewController.initComboBox(allProduct, allUnitName);
            else userShoppingListViewController.initComboBox(allProduct, allUnitName, allShoppinListName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fin Methode Listener de WindowUserShoppingListController

    //Methode Listener de WindowShoppingListControllerTools

    public void addElementOfList(Object nameProductChoose, int quantityOrNumberChoose, Object nameUnityChoose){
        shoppingListViewController.removeBorderColor();
        shoppingListViewController.showAddProductError(false);

        Product userProduct;

        if (!(Objects.equals(nameProductChoose, null) || quantityOrNumberChoose <= 0 || Objects.equals(nameUnityChoose, null))) {
            //Cree le produit pour le mettre dans le tableView
            userProduct = new Product(nameProductChoose.toString(), quantityOrNumberChoose, nameUnityChoose.toString());

            shoppingListViewController.addProductToTableView(userProduct);
            shoppingListViewController.clearElementAddProduct();
        } else {
            shoppingListViewController.showAddProductError(true);
        }
    }

    @Override
    public void returnHomeShoppingList() {
        homePageController.onShoppingListButtonClick();
    }

    public void returnToUserMenu(){
        UserMenusController userMenusController = new UserMenusController(currentStage);
        userMenusController.showAllMenus();
        //UserMenusViewController menusController = new UserMenusViewController();
//        menusController.displayMyMenus();
    }

    public void exportShoppingList(String currentShoppingListName){
        new ExportShoppingListController(currentShoppingListName);
    }

    public void sendShoppingListByMail(String currentShoppingListName){
        ShoppingList shoppingList = null;
        try {
            shoppingList = Configuration.getCurrent().getShoppingListDao().get(currentShoppingListName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MailController mailController = new MailController(shoppingList);
        mailController.initMailView();
    }

    //Fin Methode Listener de WindowUserShoppingListController

    public void initForCreateRecipe(ShoppingList shoppingList) {
        shoppingListViewController = userShoppingListViewController = new UserShoppingListViewController();

        shoppingListToSend = new ShoppingList("temporary");
        loadFXML(userShoppingListViewController, "CreateUserShoppingList.fxml");
        userShoppingListViewController.setListener(this);
        userShoppingListViewController.initForCreateRecipe(shoppingList);
        initInformationShoppingList(false);
    }

    @Override
    public void cancelRecipeCreation() {
        recipeController.modifyProductsCallback(null);
    }

    @Override
    public void returnAddedProducts() {
        recipeController.modifyProductsCallback(shoppingListToSend);
    }
}
